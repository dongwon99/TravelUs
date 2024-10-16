package com.ssafy.soltravel.v2.exception.card;

import com.ssafy.soltravel.v2.exception.CustomException;

public class InvalidCardPaymentException extends CustomException {

  private static final String DEFAULT_MESSAGE = "카드 결제가 정상적으로 처리되지 않았습니다. 네트워크 오류 또는 서버 오류가 발생할 수 있습니다.";
  private static final String DEFAULT_CODE = "CARD_PAYMENT_FAILED";
  private static final int DEFAULT_STATUS = 500;

  public InvalidCardPaymentException(String message, String code, int status, String info) {
    super(message, code, status, info);
  }
}
