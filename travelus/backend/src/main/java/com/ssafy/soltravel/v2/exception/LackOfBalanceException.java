package com.ssafy.soltravel.v2.exception;

public class LackOfBalanceException extends Throwable {

  public LackOfBalanceException(Double lastBalance, Double amount) {
    super(String.format("잔액이 부족합니다.%f%f", lastBalance, amount));
  }
}
