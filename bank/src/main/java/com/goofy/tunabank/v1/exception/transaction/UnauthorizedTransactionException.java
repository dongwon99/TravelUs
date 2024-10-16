package com.goofy.tunabank.v1.exception.transaction;

import com.goofy.tunabank.v1.exception.CustomException;

public class UnauthorizedTransactionException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Unauthorized access to the transaction";
  private static final String DEFAULT_CODE = "UNAUTHORIZED_ACCESS";
  private static final int DEFAULT_STATUS = 403;

  public UnauthorizedTransactionException(Long userId, Long accountId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s. User ID: %s, Account ID: %s", DEFAULT_MESSAGE, userId, accountId)
    );
  }
}
