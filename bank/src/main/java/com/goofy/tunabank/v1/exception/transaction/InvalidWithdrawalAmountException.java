package com.goofy.tunabank.v1.exception.transaction;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidWithdrawalAmountException extends CustomException {

  private static final String DEFAULT_MESSAGE = "withdrawal amount is not valid";
  private static final String DEFAULT_CODE = "INVALID_WITHDRAWAL_AMOUNT";
  private static final int DEFAULT_STATUS = 403;

  public InvalidWithdrawalAmountException(double amount) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s. withdrawal amount: %s", DEFAULT_MESSAGE, amount)
    );
  }

}
