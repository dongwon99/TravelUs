package com.ssafy.soltravel.v1.exception;

import lombok.Getter;

@Getter
public class PhoneNotFoundException extends RuntimeException {

  String phone;

  public PhoneNotFoundException(String phone) {
    super(String.format("잘못된 휴대폰 번호입니다: %s", phone));
  }
}
