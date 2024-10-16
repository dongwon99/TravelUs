package com.ssafy.soltravel.v2.service.settlement;

import com.ssafy.soltravel.v2.domain.BillingHistory;
import com.ssafy.soltravel.v2.domain.BillingHistoryDetail;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.SettlementType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.settlement.request.GroupSettlementHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementTransferRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementParticipantRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.response.GroupSettlementResponseDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.group.GroupMasterNotFoundException;
import com.ssafy.soltravel.v2.exception.group.InvalidGroupIdException;
import com.ssafy.soltravel.v2.exception.participant.ParticipantNotFoundException;
import com.ssafy.soltravel.v2.exception.settlement.BillingHistoryDetailNotFoundException;
import com.ssafy.soltravel.v2.exception.settlement.BillingHistoryNotFoundException;
import com.ssafy.soltravel.v2.exception.settlement.InvalidSettlementAmountException;
import com.ssafy.soltravel.v2.mapper.SettlementMapper;
import com.ssafy.soltravel.v2.repository.BillingHistoryDetailRepository;
import com.ssafy.soltravel.v2.repository.BillingHistoryRepository;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.ParticipantRepository;
import com.ssafy.soltravel.v2.service.NotificationService;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.group.GroupService;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementService {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final GroupService groupService;
    private final BillingHistoryRepository billingHistoryRepository;
    private final BillingHistoryDetailRepository billingHistoryDetailRepository;
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;

    private final SecurityUtil securityUtil;
    private final SettlementMapper settlementMapper;
    private final NotificationService notificationService;

    @Transactional
    public String executeSettlement(SettlementRequestDto settlementRequestDto) {

        SettlementType type = settlementRequestDto.getSettlementType();
        AccountDto account = accountService.getByAccountNo(settlementRequestDto.getAccountNo());

        GroupDto group = groupService.getGroupInfo(settlementRequestDto.getGroupId());
        List<ParticipantDto> participants = group.getParticipants();
        long masterUserId = participants.stream().filter(ParticipantDto::isMaster).findFirst()
            .orElseThrow(() -> new GroupMasterNotFoundException(group.getGroupId())).getUserId();

        String response = "success";
        switch (type) {
            case G -> {
                settleOnlyKRW(settlementRequestDto, masterUserId);
            }
            case F -> {
                settleOnlyForeign(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(),
                    masterUserId);
            }
            case BOTH -> response = settleBoth(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(),
                masterUserId);
        }

        paySettlementToMembers(group.getGroupName(), participants, settlementRequestDto.getParticipants());

        return response;
    }

    /**
     * 1. 원화만 정산
     */
    @Transactional
    public void settleOnlyKRW(SettlementRequestDto settlementRequestDto, long masterUserId) {

        /**
         * 2.원화 출금
         * 3.개인 입금
         */
        transactionService.postAccountWithdrawal(
            new TransactionRequestDto(settlementRequestDto.getAccountNo(), settlementRequestDto.getAccountPassword(),
                CurrencyType.KRW, TransactionType.SW, settlementRequestDto.getAmounts().get(0), "자동 정산 출금"),
            masterUserId);
    }

    /**
     * 2. 외화만 정산
     */
    @Transactional
    public void settleOnlyForeign(SettlementRequestDto settlementRequestDto, CurrencyType currencyCode,
        long masterUserId) {

        /**
         * 2.외화 정산출금
         * 3.개인 정산입금
         */
        transactionService.postAccountWithdrawal(
            new TransactionRequestDto(settlementRequestDto.getAccountNo(), settlementRequestDto.getAccountPassword(),
                currencyCode,
                TransactionType.SW, settlementRequestDto.getAmounts().get(1), "자동 정산 출금"), masterUserId);
    }

    /**
     * 모두 정산
     */
    @Transactional
    public String settleBoth(SettlementRequestDto settlementRequestDto, CurrencyType currencyCode, long masterUserId) {

        /**
         * 1.재환전
         * 2.원화 정산출금
         * 3.개인 정산입금
         */

        List<TransferHistoryResponseDto> response = transactionService.postMoneyBoxTransfer(
            MoneyBoxTransferRequestDto.create(TransferType.M, settlementRequestDto.getAccountNo(),
                settlementRequestDto.getAccountPassword(), currencyCode, CurrencyType.KRW,
                settlementRequestDto.getAmounts().get(1)),
            false, -1).getBody();

        BigDecimal transactionAmount = new BigDecimal(response.get(1).getTransactionAmount()); // 재환전된 원화 금액
        BigDecimal krwAmount = new BigDecimal(settlementRequestDto.getAmounts().get(0)); // 원화 금액
        BigDecimal transactionBalance = transactionAmount.add(krwAmount);

        transactionService.postAccountWithdrawal(
            new TransactionRequestDto(settlementRequestDto.getAccountNo(), settlementRequestDto.getAccountPassword(),
                CurrencyType.KRW, TransactionType.SW, transactionBalance.doubleValue(), "자동 정산 출금"), masterUserId);

        return response.get(0).getTransactionSummary();
    }

    /**
     * 개인 정산금 지급
     */
    @Transactional
    public void paySettlementToMembers(String groupName, List<ParticipantDto> participants,
        List<SettlementParticipantRequestDto> requestDtos) {

        for (SettlementParticipantRequestDto requestDto : requestDtos) {

            long participantId = requestDto.getParticipantId();
            ParticipantDto participant = participants.stream().filter(p -> p.getParticipantId().equals(participantId))
                .findFirst()
                .orElseThrow(() -> new ParticipantNotFoundException(participantId));

            String accountNo = participant.getPersonalAccountNo();

            transactionService.postAccountDeposit(
                new TransactionRequestDto(accountNo, null, CurrencyType.KRW, TransactionType.SD, requestDto.getAmount(),
                    String.format("[%s] 자동 정산 입금", groupName)), participant.getUserId());
        }
    }

    /**
     * 개별 정산 알림 요청 메서드
     */
    @Transactional
    public String registerPersonalSettlement(PersonalSettlementRegisterRequestDto requestDto) {

        List<SettlementParticipantRequestDto> participants = requestDto.getParticipants();
        LocalDateTime now = LocalDateTime.now();

        long groupId = requestDto.getGroupId();
        TravelGroup group = groupRepository.findById(groupId).orElseThrow(() -> new InvalidGroupIdException());
        List<Participant> realParticipants = group.getParticipants();

        BillingHistory billingHistory = BillingHistory.createBillingHistory(group, requestDto.getTotalAmount(), now);
        BillingHistory billingHistorySaved = billingHistoryRepository.save(billingHistory);

        // 알림 메시지를 보낼 정보를 담은 detail 리스트
        List<BillingHistoryDetail> details = new ArrayList<>();

        double totalAmount = 0;

        for (SettlementParticipantRequestDto request : participants) {

            Participant participant = participantRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new ParticipantNotFoundException(request.getParticipantId()));

            totalAmount += request.getAmount();
            //TODO: 그룹의 참여자가 맞는지 확인할 예정

            BillingHistoryDetail detail = BillingHistoryDetail.createBillingHistoryDetail(billingHistorySaved,
                participant,
                request.getAmount(), now);

            details.add(detail);

            billingHistoryDetailRepository.save(detail);
        }

        if (totalAmount != requestDto.getTotalAmount()) {
            throw new InvalidSettlementAmountException(totalAmount);
        }

        //TODO: 알림 보내기(for문 도시게)

        for (BillingHistoryDetail detail : details) {
            notificationService.sendPersonalSettlementNotification(group, detail, billingHistory.getId());
        }

        return "success";
    }

    /**
     * 개인별 - 개별 정산 내역 조회 메서드
     */
    public List<PersonalSettlementDto> getPersonalSettlementHistory(PersonalSettlementHistoryRequestDto requestDto) {

        long userId = securityUtil.getCurrentUserId();

        List<BillingHistoryDetail> response = billingHistoryDetailRepository.findByDynamicSettlementStatus(userId,
            requestDto.getSettlementStatus()).orElseThrow(() -> new BillingHistoryDetailNotFoundException(userId));

        return settlementMapper.toPersonalSettlementDtos(response);
    }

    /**
     * 모임별 - 개별 정산 내역 조회
     */
    public List<GroupSettlementResponseDto> getGroupSettlementHistory(GroupSettlementHistoryRequestDto requestDto) {

        long groupId = requestDto.getGroupId();
        List<BillingHistory> response = billingHistoryRepository.findByGroup_GroupId(groupId)
            .orElseThrow(() -> new BillingHistoryNotFoundException(groupId));

        return settlementMapper.toGroupSettlementResponseDtos(response);
    }

    /**
     * 개별 정산 요청 건당 조회 메서드
     */
    public GroupSettlementResponseDto getSettlementHistory(long settlementId) {

        BillingHistory history = billingHistoryRepository.findById(settlementId)
            .orElseThrow(() -> new BillingHistoryNotFoundException(settlementId));

        return settlementMapper.toGroupSettlementResponseDto(history);
    }

    /**
     * 개별 정산금 이체 메서드
     */
    @Transactional
    public ResponseEntity<List<TransferHistoryResponseDto>> postPersonalSettlementTransfer(
        PersonalSettlementTransferRequestDto requestDto) {

        double transactionBalance = requestDto.getTransactionBalance();
        LocalDateTime now = LocalDateTime.now();

        ResponseEntity<List<TransferHistoryResponseDto>> response = transactionService.postGeneralTransfer(
            TransferRequestDto.builder().transferType(TransferType.G)
                .withdrawalAccountNo(requestDto.getWithdrawalAccountNo())
                .withdrawalTransactionSummary(requestDto.getWithdrawalTransactionSummary())
                .depositAccountNo(requestDto.getDepositAccountNo())
                .depositTransactionSummary(requestDto.getDepositTransactionSummary())
                .accountPassword(requestDto.getAccountPassword())
                .transactionBalance(transactionBalance).build());

        long detailId = requestDto.getSettlementDetailId();
        BillingHistoryDetail detail = billingHistoryDetailRepository.findById(detailId)
            .orElseThrow(() -> new BillingHistoryDetailNotFoundException(detailId));

        double currentRemainingAmount = detail.getRemainingAmount();
        double afterDetailRemainingAmount = currentRemainingAmount - transactionBalance;

        detail.setRemainingAmount((afterDetailRemainingAmount) > 0 ? afterDetailRemainingAmount : 0);
        detail.setUpdatedAt(now);
        billingHistoryDetailRepository.save(detail);

        BillingHistory history = detail.getBillingHistory();
        double totalRemainingAmount = history.getRemainingAmount();
        double afterHistoryRemainingAmount = totalRemainingAmount - transactionBalance;
        history.setRemainingAmount(afterHistoryRemainingAmount > 0 ? afterHistoryRemainingAmount : 0);
        history.setUpdatedAt(now);
        billingHistoryRepository.save(history);

        return response;
    }
}