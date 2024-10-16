package com.ssafy.soltravel.v2.exception.account;

public class InvalidPersonalAccountException extends RuntimeException {

  public InvalidPersonalAccountException() {
    super(String.format("모임에서 정산 받을 계좌는 개인 계좌만 등록 가능합니다."));
  }
}
