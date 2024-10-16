package com.ssafy.soltravel.v1.service;

import com.ssafy.soltravel.v1.dto.notification.ExchangeNotificationDto;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeResponseDto;
import com.ssafy.soltravel.v1.dto.notification.TransactionNotificationDto;
import com.ssafy.soltravel.v1.dto.settlement.SettlementResponseDto;
import com.ssafy.soltravel.v1.service.account.AccountService;
import com.ssafy.soltravel.v1.util.LogUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final AccountService accountService;
  private final RedisTemplate<String, SseEmitter> redisTemplate; // RedisTemplate을 사용하여 Redis에 접근

  // Redis에서 구독 정보의 키에 사용할 접두사
  private static final String EMITTER_PREFIX = "EMITTER_";

  /**
   * 메시지 알림 구독
   */
  public SseEmitter subscribe(long userId) {

    LogUtil.info("알림구독요청",userId);
    //sseEmitter 객체 생성
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    //연결
    try {
      sseEmitter.send(SseEmitter.event().name("connect"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Redis에 저장
    redisTemplate.opsForValue().set(EMITTER_PREFIX + userId, sseEmitter, 2400, TimeUnit.HOURS); // 24시간 동안 유효

    sseEmitter.onCompletion(() -> redisTemplate.delete(EMITTER_PREFIX + userId));  // sseEmitter 연결 완료 시 제거
    sseEmitter.onTimeout(() -> redisTemplate.delete(EMITTER_PREFIX + userId));    // sseEmitter 연결 타임아웃 시 제거
    sseEmitter.onError((e) -> redisTemplate.delete(EMITTER_PREFIX + userId));    // sseEmitter 연결 오류 시 제거

    return sseEmitter;
  }

  /**
   * Redis에서 SseEmitter 가져오기
   */
  private SseEmitter getEmitter(long userId) {
    return redisTemplate.opsForValue().get(EMITTER_PREFIX + userId);
  }

  /**
   * 환전 알림
   */
  public void notifyExchangeMessage(ExchangeResponseDto exchangeResponseDto) {

    String accountNo=exchangeResponseDto.getAccountInfoDto().getAccountNo();

    List<Long> participants=accountService.findUserIdsByGeneralAccountId(exchangeResponseDto.getAccountInfoDto().getAccountId());

    for(long userId : participants) {

      SseEmitter sseEmitterReceiver = getEmitter(userId);

      if (sseEmitterReceiver != null) {
        //알림 전송
        try {
          String message = String.format("고객님의 모임계좌[%s]에 환전이 실행되었습니다.", accountNo);

          ExchangeNotificationDto dto = new ExchangeNotificationDto(
              exchangeResponseDto.getAccountInfoDto().getAccountId(),
              accountNo,
              exchangeResponseDto.getExchangeCurrencyDto().getExchangeRate().toString(),
              message
          );
          sseEmitterReceiver.send(SseEmitter.event().name("Exchange").data(dto));
        } catch (Exception e) {
          redisTemplate.delete(EMITTER_PREFIX + userId);
        }
      }
    }
  }

  /**
   * 정산 알림
   */
  public void notifySettlementMessage(SettlementResponseDto settlementResponseDto) {

    long userId = settlementResponseDto.getUserId();

    SseEmitter sseEmitterReceiver = getEmitter(userId);

    if (sseEmitterReceiver != null) {
      //알림 전송
      try {
        sseEmitterReceiver.send(SseEmitter.event().name("Settlement").data(settlementResponseDto));
      } catch (Exception e) {
        redisTemplate.delete(EMITTER_PREFIX + userId);
      }
    }
  }

  /**
   * 입금 알림
   */
  public void notifyTransactionMessage(TransactionNotificationDto transactionNotificationDto) {

    long userId = transactionNotificationDto.getUserId();

    SseEmitter sseEmitterReceiver = getEmitter(userId);

    if (sseEmitterReceiver != null) {
      //알림 전송
      try {
        sseEmitterReceiver.send(SseEmitter.event().name("Transaction").data(transactionNotificationDto));
      } catch (Exception e) {
        redisTemplate.delete(EMITTER_PREFIX + userId);
      }
    }
  }


  public void notifyAllUser(){


    for(String uId: redisTemplate.keys(EMITTER_PREFIX + "*")) {
      Long userId = Long.valueOf(uId.substring(8, uId.length()));
      LogUtil.info("for userId", userId);

      SseEmitter sseEmitterReceiver = getEmitter(userId);


      if (sseEmitterReceiver != null) {
        LogUtil.info("for SseEmitter", sseEmitterReceiver.toString());

        try {
          sseEmitterReceiver.send(SseEmitter.event().name("all").data("notify!!!!!!"));
        } catch (Exception e) {
          redisTemplate.delete(EMITTER_PREFIX + userId);
        }
      }
    }

  }
}
