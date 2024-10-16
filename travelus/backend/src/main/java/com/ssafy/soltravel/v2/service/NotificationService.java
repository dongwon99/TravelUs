package com.ssafy.soltravel.v2.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.ssafy.soltravel.v1.dto.user.UserDetailDto;
import com.ssafy.soltravel.v2.domain.BillingHistoryDetail;
import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.domain.Enum.NotificationType;
import com.ssafy.soltravel.v2.domain.Notification;
import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.domain.redis.RedisFcm;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.exchange.targetAccountDto;
import com.ssafy.soltravel.v2.dto.notification.NotificationDto;
import com.ssafy.soltravel.v2.dto.notification.PushNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.notification.RegisterNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.exception.group.InvalidGroupIdException;
import com.ssafy.soltravel.v2.exception.notification.NotificationNotFoundException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.mapper.NotificationMapper;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.NotificationRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.repository.redis.FcmTokenRepository;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    // 기본 알림 아이콘
    private static final String DEFAULT_ICON_URL = "/icons/favicon.ico";
    // 소술점 0인경우 떼기 ex) 1000.00 -> 1000
    private static final DecimalFormat df = new DecimalFormat("#,###.##");

    private final AccountService accountService;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final SecurityUtil securityUtil;


    /*
     * FCM 토큰 레디스 저장
     */
    public ResponseDto saveFcmToken(RegisterNotificationRequestDto requestDto) {

        User user = securityUtil.getUserByToken();

        fcmTokenRepository.save(new RedisFcm(user.getUserId(), requestDto.getFcmToken()));

        return new ResponseDto();
    }

    /*
     * FCM 토큰 레디스 삭제
     */
    public ResponseDto deleteFcmToken(Long userId) {

        fcmTokenRepository.deleteById(userId);

        return new ResponseDto();
    }

    /*
     * ===== 알림 전송 공통 메서드 =====
     */

    // 공통 푸시 알림 생성 메서드
    private Message createMessage(String token, PushNotificationRequestDto requestDto) {

        LogUtil.info("requestDto", requestDto);

        return Message.builder()
            .setToken(token)  // 대상 사용자 토큰
            .setWebpushConfig(WebpushConfig.builder()
                .putHeader("ttl", "300")  // 타임 투 라이브 설정 (예: 300초)
                .setNotification(WebpushNotification.builder() // 알림 내용 생성
                    .setTitle(requestDto.getTitle())
                    .setBody(requestDto.getMessage())
                    .setIcon(DEFAULT_ICON_URL)
                    .build())
                .build())
            .putData("notificationType", requestDto.getNotificationType().toString()) // 알림 외 필요 데이터 추가
            .putData("groupId", String.valueOf(requestDto.getGroupId()))
            .putData("accountNo", requestDto.getAccountNo() != null ? requestDto.getAccountNo() : "")
            .putData("currencyCode",
                requestDto.getCurrencyType() != null ? requestDto.getCurrencyType().getCurrencyCode() : "")
            .putData("settlementId", requestDto.getSettlementId() != null ? String.valueOf(requestDto.getSettlementId()) : "")
            .build();
    }

    // 알림 전송 공통 메서드 - 사용자에게 알림 푸시
    public ResponseEntity<?> pushNotification(PushNotificationRequestDto requestDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        LogUtil.info("requestDto", requestDto);

        try {
            // FCM 토큰 조회
            RedisFcm redisFcm = fcmTokenRepository.findById(requestDto.getTargetUserId()).orElse(null);

            if (redisFcm == null) {
                // 토큰이 없는 경우 로그만 남기고 알림 전송 건너뜀
                LogUtil.info("알림을 보내려고하는 유저의 토큰이 존재 하지 않습니다. 로그인상태를 확인해주세요");
                return null; // 또는 적절한 로직으로 넘어가기
            }
            // 메시지 생성 및 전송
            Message message = createMessage(redisFcm.getFcmToken(), requestDto);

            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            status = HttpStatus.OK;
            resultMap.put("response", response);

        } catch (Exception e) {
            resultMap.put("message", "요청 실패");
            resultMap.put("exception", e.getMessage());
        }

        // 알림 저장
        saveNotification(requestDto);

        return new ResponseEntity<>(resultMap, status);
    }

    // 알림 저장 공통 메서드 - DB에 생성된 알림 저장
    public void saveNotification(PushNotificationRequestDto requestDto) {

        User user = userRepository.findByUserId(requestDto.getTargetUserId())
            .orElseThrow(() -> new UserNotFoundException(requestDto.getTargetUserId()));

        TravelGroup group = null;

        if (requestDto.getGroupId() != null) {
            group = groupRepository.findById(requestDto.getGroupId()).orElseThrow(InvalidGroupIdException::new);
        }

        Notification notification = Notification.createNotification(user, group, requestDto);

        notificationRepository.save(notification);
    }

    /*
     * ===== 거래 종류 별 알림 전송 메서드 =====
     */
    public ResponseEntity<?> sendDepositNotification(Long userId, TransactionRequestDto requestDto) {

        String title;
        String message;
        String formattedAmount = df.format(requestDto.getTransactionBalance());

        LogUtil.info("TransactionRequestDto", requestDto);

        // 트랜잭션 타입에 따라 switch-case로 알림 제목 및 메시지 설정
        switch (requestDto.getTransactionType()) {
            case D -> {
                title = "입금 완료";
                message = String.format("입금 금액: %s %s이 입금되었습니다.", formattedAmount, requestDto.getCurrencyCode());
            }
            case W -> {
                title = "출금 완료";
                message = String.format("출금 금액: %s %s이 출금되었습니다.", formattedAmount, requestDto.getCurrencyCode());
            }
            case SD -> {
                title = "정산 입금 완료";
                message = String.format("정산 입금 금액: %s %s이 입금되었습니다.", formattedAmount,
                    requestDto.getCurrencyCode());
            }
            case SW -> {
                title = "정산 출금 완료";
                message = String.format("정산 출금 금액: %s %s이 출금되었습니다.", formattedAmount,
                    requestDto.getCurrencyCode());
            }
            default -> {
                throw new IllegalArgumentException("지원되지 않는 트랜잭션 타입입니다.");
            }
        }

        LogUtil.info("message", message);

        AccountDto accountDto = accountService.getByAccountNo(requestDto.getAccountNo());

        NotificationType notificationType = NotificationType.PT;

        TravelGroup group = null;

        if (accountDto.getAccountType() == AccountType.G) {
            title = "모임통장 " + title;
            message = "모임통장 " + message;
            notificationType = NotificationType.GT;

            group = groupRepository.findByGroupAccountNo(requestDto.getAccountNo());
        }

        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.builder()
            .targetUserId(userId)
            .notificationType(notificationType)
            .title(title)
            .message(message)
            .accountNo(requestDto.getAccountNo())
            .groupId((group != null) ? group.getGroupId() : null)
            .currencyType(requestDto.getCurrencyCode())
            .build();

        return pushNotification(notificationRequestDto);
    }

    /*
     * 이체 알림
     */
    public ResponseEntity<?> sendTransferNotification(User user, TransferRequestDto requestDto) {

        // 돈을 받는 사람 유저 정보
        UserDetailDto depositUser = accountService.getUserByAccountNo(requestDto.getDepositAccountNo());

        String formattedAmount = df.format(requestDto.getTransactionBalance());

        String title = "이체 완료";
        String message = String.format("%s님에게 %s원을 보냈어요.", depositUser.getName(), formattedAmount);

        AccountDto senderAccount = accountService.getByAccountNo(requestDto.getWithdrawalAccountNo());
        AccountDto depositAccount = accountService.getByAccountNo(requestDto.getDepositAccountNo());

        NotificationType notificationType = NotificationType.PT;
        TravelGroup group = null;

        if (depositAccount.getAccountType().equals(AccountType.G)) {
            group = groupRepository.findByGroupAccountNo(requestDto.getDepositAccountNo());

            title = "모임통장 " + title;
            message = String.format("\"%s\" 모임통장 으로 %s원을 보냈어요.", group.getGroupName(), formattedAmount);

            notificationType = NotificationType.GT;
        }

        // 돈을 보내는 사람
        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            user.getUserId(),
            notificationType,
            title,
            message,
            requestDto.getWithdrawalAccountNo(),
            (group != null) ? group.getGroupId() : null
        );

        // 알림 전송
        pushNotification(notificationRequestDto);

        title = user.getName() + "님이 돈을 보냈어요";
        message = String.format("%s원이 튜나은행 계좌로 입금되었어요.", formattedAmount);

        notificationType = NotificationType.PT;

        TravelGroup depositGroup = null;

        if (depositAccount.getAccountType().equals(AccountType.G)) {
            depositGroup = groupRepository.findByGroupAccountNo(requestDto.getDepositAccountNo());

            message = String.format("%s원이 \"%s\" 모임통장으로 입금되었어요.", formattedAmount, depositGroup.getGroupName());
            notificationType = NotificationType.GT;
        }

        PushNotificationRequestDto depositNotificationRequestDto = PushNotificationRequestDto.createDto(
            depositUser.getId(),
            notificationType,
            title,
            message,
            requestDto.getDepositAccountNo(),
            (depositGroup != null) ? depositGroup.getGroupId() : null
        );

        // 알림 전송
        pushNotification(depositNotificationRequestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }

    /*
     * 자동 환전 알림 전송
     */
    public ResponseEntity<?> sendAutoExchangeNotification(
        targetAccountDto targetAccountDto,
        MoneyBoxTransferRequestDto requestDto
    ) {

        String formattedAmount = df.format(requestDto.getTransactionBalance());

        String title = "자동 환전 완료";
        String message = String.format("설정 환율 %s에 도달하여 %s%s이 성공적으로 환전되었습니다.",
            targetAccountDto.getTargetRate(), formattedAmount, requestDto.getSourceCurrencyCode());

        TravelGroup group = groupRepository.findByGroupAccountNo(targetAccountDto.getAccountNo());

        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            targetAccountDto.getUserId(),
            NotificationType.E,
            title,
            message,
            targetAccountDto.getAccountNo(),
            group.getGroupId(),
            requestDto.getTargetCurrencyCode(),
            null
        );

        // 알림 전송
        pushNotification(notificationRequestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }

    /*
     *  환전 알림 전송
     */
    public ResponseEntity<?> sendExchangeNotification(
        User user,
        MoneyBoxTransferRequestDto requestDto
    ) {

        String formattedAmount = df.format(requestDto.getTransactionBalance());
        String title = "환전 완료";
        String message = String.format("%s%s 이 성공적으로 환전되었습니다.", formattedAmount, requestDto.getSourceCurrencyCode());

        TravelGroup group = groupRepository.findByGroupAccountNo(requestDto.getAccountNo());

        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            user.getUserId(),
            NotificationType.E,
            title,
            message,
            requestDto.getAccountNo(),
            group.getGroupId(),
            requestDto.getTargetCurrencyCode(),
            null
        );

        // 알림 전송
        pushNotification(notificationRequestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }

    /*
     *  개별 정산 알림
     */
    public ResponseEntity<?> sendPersonalSettlementNotification(
        TravelGroup group, BillingHistoryDetail requestDto, Long settlementId
    ) {

        String title = "새로운 정산 요청이 들어왔어요!";

        String formattedAmount = df.format(requestDto.getAmount());

        String message = String.format("%s에서 %s원 정산을 요청했어요.",
            group.getGroupName(),
            formattedAmount
        );

        Participant participant = requestDto.getParticipant();

        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            participant.getUser().getUserId(),
            NotificationType.S,
            title,
            message,
            group.getGroupAccountNo(),
            group.getGroupId(),
            null,
            settlementId
        );

        // 알림 전송
        pushNotification(notificationRequestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }

    /*
     *  여행 전 환전 여부 알림 -> 7일 전인데 환전 안했으면 알림 보냄
     */
    public ResponseEntity<?> sendExchangeSuggestNotification(
        PushNotificationRequestDto requestDto
    ) {

        TravelGroup group = groupRepository.findByGroupAccountNo(requestDto.getAccountNo());
        // 알림 전송
        pushNotification(requestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }


    public List<NotificationDto> getAllByUserId() {

        User user = securityUtil.getUserByToken();

        List<Notification> notificationList = notificationRepository.findAllByUser_userId(user.getUserId());

        return notificationMapper.toDtoList(notificationList);
    }

    public ResponseDto readAllNotifications() {

        User user = securityUtil.getUserByToken();

        notificationRepository.readAllNotifications(user.getUserId());

        return new ResponseDto();
    }

    public ResponseDto deleteNotification(Long notificationId) {
        User user = securityUtil.getUserByToken();

        notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        notificationRepository.deleteById(notificationId);

        return new ResponseDto();

    }

}


