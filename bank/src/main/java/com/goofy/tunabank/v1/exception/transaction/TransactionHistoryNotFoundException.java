package com.goofy.tunabank.v1.exception.transaction;

import com.goofy.tunabank.v1.exception.CustomException;

public class TransactionHistoryNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "transaction history not found";
  private static final String DEFAULT_CODE = "TRANSACTION_HISTORY_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public TransactionHistoryNotFoundException() {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s.", DEFAULT_MESSAGE)
    );
  }
}
