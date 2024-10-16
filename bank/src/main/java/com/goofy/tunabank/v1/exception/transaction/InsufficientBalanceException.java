package com.goofy.tunabank.v1.exception.transaction;

import com.goofy.tunabank.v1.exception.CustomException;

public class InsufficientBalanceException extends CustomException {

  private static final String DEFAULT_MESSAGE = "balance is insufficient";
  private static final String DEFAULT_CODE = "INSUFFICIENT_BALANCE";
  private static final int DEFAULT_STATUS = 403;

  public InsufficientBalanceException(double balance, double amount) {

    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s. 잔액: %s, 출금 거래 금액: %s", DEFAULT_MESSAGE, balance, amount)
    );
  }
}
