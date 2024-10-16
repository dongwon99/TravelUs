package com.goofy.tunabank.v1.exception.card;

import com.goofy.tunabank.v1.exception.CustomException;

public class DuplicateTransactionIdException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Duplicate transaction ID detected.";
  private static final String DEFAULT_CODE = "DUPLICATE_TRANSACTION";
  private static final int DEFAULT_STATUS = 409;

  public DuplicateTransactionIdException(String transactionId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, transactionId)
    );
  }
}
