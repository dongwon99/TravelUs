package com.ssafy.soltravel.v2.exception;

public class InvalidAmountException extends RuntimeException {

  public InvalidAmountException(double amount, String Currency) {
    super(String.format("최소 환전 금액은 %.2f %s입니다.", amount, Currency));
  }
}
