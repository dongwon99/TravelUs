package com.ssafy.soltravel.v1.service.settlement;

import com.ssafy.soltravel.v1.domain.ForeignAccount;
import com.ssafy.soltravel.v1.domain.GeneralAccount;
import com.ssafy.soltravel.v1.domain.Participant;
import com.ssafy.soltravel.v1.dto.settlement.SettlementRequestDto;
import com.ssafy.soltravel.v1.dto.settlement.SettlementResponseDto;
import com.ssafy.soltravel.v1.dto.transaction.request.ForeignTransactionRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v1.repository.ForeignAccountRepository;
import com.ssafy.soltravel.v1.repository.ParticipantRepository;
import com.ssafy.soltravel.v1.service.NotificationService;
import com.ssafy.soltravel.v1.service.exchange.ExchangeService;
import com.ssafy.soltravel.v1.service.transaction.TransactionService;
import com.ssafy.soltravel.v1.util.LogUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementService {

  private final ExchangeService exchangeService;
  private final TransactionService transactionService;
  private final NotificationService notificationService;
  private final ForeignAccountRepository foreignAccountRepository;
  private final ParticipantRepository participantRepository;


  public ResponseEntity<?> executeSettlement(SettlementRequestDto requestDto) {

    ForeignAccount foreignAccount = foreignAccountRepository.findById(requestDto.getAccountId())
        .orElseThrow();

    GeneralAccount generalAccount = foreignAccount.getGeneralAccount();
    long amount = exchangeService.convertUsdToKrwWithoutFee(foreignAccount.getBalance(),
        exchangeService.getExchangeRate(foreignAccount.getCurrency().getCurrencyCode())
            .getExchangeRate());
//
//    LogUtil.info("<<정산 실행>> 외화 계좌 정보: ", foreignAccount.toString());
//    LogUtil.info("<<정산 실행>> 모임 계좌 정보: ", generalAccount.toString());
    LogUtil.info("<<정산 실행>>모임통장에 입금될 convertUsdToWithoutFee를 통해 계산된 amount(원): ", amount);

    if (amount > 0) {
      //외화 계좌에서 출금
      ForeignTransactionRequestDto foreignTransactionRequestDto = new ForeignTransactionRequestDto();
      foreignTransactionRequestDto.setTransactionBalance(foreignAccount.getBalance());
      foreignTransactionRequestDto.setTransactionSummary("정산 출금");
      foreignTransactionRequestDto.setUserId(
          foreignAccount.getGeneralAccount().getUser().getUserId());//모임주의 id
      transactionService.postForeignWithdrawal(true, requestDto.getAccountNo(),
          foreignTransactionRequestDto);

      LogUtil.info("<<정산 실행>> 외화 계좌 번호:", requestDto.getAccountNo());
      LogUtil.info("<<정산 실행>> 외화 계좌에서 출금되는 금액:",
          foreignTransactionRequestDto.getTransactionBalance());
      LogUtil.info("<<정산 실행>> 외화 계좌 모임주의 id:", foreignTransactionRequestDto.getUserId());
      LogUtil.info("<<정산 실행>> transactionService.postForeignWithdrawal에 넘기는 dto:",
          foreignTransactionRequestDto.toString());

      //일반 통장에 입금
      TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
      transactionRequestDto.setTransactionBalance(amount);
      transactionRequestDto.setTransactionSummary("정산 입금");
      transactionRequestDto.setUserId(generalAccount.getUser().getUserId());//계좌 주인의 Id
      transactionService.postAccountDeposit(generalAccount.getAccountNo(), transactionRequestDto);

      LogUtil.info("<<정산 실행>> 모임 계좌 번호:", generalAccount.getAccountNo());
      LogUtil.info("<<정산 실행>> 모임 계좌로 입금되는 금액:",
          transactionRequestDto.getTransactionBalance());
      LogUtil.info("<<정산 실행>> 모임 계좌 모임주의 id:", generalAccount.getUser().getUserId());
    }

    //정산
    divideBalance(generalAccount);

    return ResponseEntity.status(HttpStatus.OK).body("정산 완료");
  }


  public void divideBalance(GeneralAccount generalAccount) {


    LogUtil.info("<<모임원 정산 금액 나누기 시작>>");
    LogUtil.info("<<모임원 정산 금액 계산>> 모임 계좌 id: ", generalAccount.getId());
    LogUtil.info("<<모임원 정산 금액 계산>> 모임 계좌 잔액: ", generalAccount.getBalance());


    long accountId = generalAccount.getId();
    String accountNo = generalAccount.getAccountNo();
    List<Participant> participants = participantRepository.findAllByGeneralAccountId(accountId);

    for (Participant participant : participants) {
      LogUtil.info("<<모임원 정산 금액 계산>> 모임원 계좌 번호 조회: ",
          participant.getGeneralAccount().getAccountNo());
    }

    //나눠진 잔액
    double originAmount = generalAccount.getBalance();
    long amountPerPerson = (long) Math.floor(originAmount / participants.size());

    //TODO: 일반 모임 통장 전액 출금
    double balance = generalAccount.getBalance();
    long withdrawalAmount = (long) balance;

    TransactionRequestDto withdrawal = new TransactionRequestDto();
    withdrawal.setTransactionBalance(withdrawalAmount);//원화
    withdrawal.setTransactionSummary("모임원 정산 출금");
    withdrawal.setUserId(generalAccount.getUser().getUserId());
    transactionService.postAccountWithdrawal(generalAccount.getAccountNo(), withdrawal);

    LogUtil.info("<<정산 실행>> 모임 계좌 전액 출금 계좌 :", generalAccount.getAccountNo());
    LogUtil.info("<<정산 실행>> 모임 계좌 잔액 :", withdrawalAmount);
    LogUtil.info("<<정산 실행>> 모임 계좌 출금 액:", withdrawal.getTransactionBalance());

    /**
     * 각 모임원에게 입금
     */
    LogUtil.info("각 모임원 입금 금액(amountPerPerson):", amountPerPerson);
    for (Participant participant : participants) {

      //TODO: 개인 계좌 입금으로 변경
//      TransferRequestDto transferRequestDto = new TransferRequestDto();
//      transferRequestDto.setDepositAccountNo(participant.getPersonalAccount().getAccountNo());
//      transferRequestDto.setDepositTransactionSummary("정산 입금");
//      transferRequestDto.setTransactionBalance(amountPerPerson);
//      transferRequestDto.setWithdrawalTransactionSummary("정산 출금");
//
//
//      LogUtil.info("<<각 모임원 이체 중>>");
//      LogUtil.info("<<각 모임원 이체 중>> 모임 계좌 번호:",transferRequestDto.getDepositAccountNo());
//      LogUtil.info("<<각 모임원 이체 중>> 모임원 개인 계좌 번호:",transferRequestDto.getDepositAccountNo());
//      LogUtil.info("<<각 모임원 이체 중>> 모임 계좌 에서 출금(이체)되는 금액:",transferRequestDto.getDepositAccountNo());
//      transactionService.postAccountTransfer(generalAccount.getAccountNo(),transferRequestDto);

      TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
      transactionRequestDto.setTransactionBalance(amountPerPerson);
      transactionRequestDto.setTransactionSummary("정산 입금");
      transactionRequestDto.setUserId(participant.getUser().getUserId());
      transactionService.postAccountDeposit(participant.getPersonalAccount().getAccountNo(),
          transactionRequestDto);

      //알림 전송
      SettlementResponseDto responseDto = new SettlementResponseDto();
      responseDto.setUserId(participant.getUser().getUserId());
      responseDto.setAccountId(accountId);
      responseDto.setAccountNo(accountNo);
      responseDto.setAmountPerPerson(amountPerPerson);
      responseDto.setMessage(String.format("고객님의 모임계좌[%s]에 정산이 실행되었습니다.", accountNo));

      notificationService.notifySettlementMessage(responseDto);
    }
  }
}
