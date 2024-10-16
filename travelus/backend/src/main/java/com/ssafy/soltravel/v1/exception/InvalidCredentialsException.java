package com.ssafy.soltravel.v1.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {

  private String email;

  public InvalidCredentialsException(String email) {
    super(String.format("아이디 또는 비밀번호가 틀렸습니다: %s", email));
    this.email = email;
  }
}
