package com.ssafy.soltravel.v1.exception;

import lombok.Getter;

@Getter
public class ForeignAccountNotFoundException extends RuntimeException {

  private String accountNo;
  public ForeignAccountNotFoundException(String accountNo) {
    super(String.format("Foreign Account Not Found: %s", accountNo));
    this.accountNo = accountNo;
  }
}
