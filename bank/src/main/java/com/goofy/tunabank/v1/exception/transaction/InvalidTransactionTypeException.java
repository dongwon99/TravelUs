package com.goofy.tunabank.v1.exception.transaction;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidTransactionTypeException extends CustomException {

  private static final String DEFAULT_MESSAGE = "transactionType is not valid";
  private static final String DEFAULT_CODE = "INVALID_TRANSACTION_TYPE";
  private static final int DEFAULT_STATUS = 403;

  public InvalidTransactionTypeException(TransactionType type) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s. transactionType: %s", DEFAULT_MESSAGE, type)
    );
  }
}
