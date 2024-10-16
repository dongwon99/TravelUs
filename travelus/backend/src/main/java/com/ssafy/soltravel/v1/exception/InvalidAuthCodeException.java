package com.ssafy.soltravel.v1.exception;

import lombok.Getter;

@Getter
public class InvalidAuthCodeException extends RuntimeException {

  public InvalidAuthCodeException(String phone, String authCode) {
    super(String.format("인증코드(%s)가 틀렸습니다: %s", authCode, phone));
  }

}
