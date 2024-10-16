package com.goofy.tunabank.v1.exception.auth;

import com.goofy.tunabank.v1.exception.CustomException;

public class ApiKeyLimitExceededException extends CustomException {

  private static final String DEFAULT_MESSAGE = "User already has 5 or more API keys";
  private static final String DEFAULT_CODE = "API_KEY_LIMIT_EXCEEDED";
  private static final int DEFAULT_STATUS = 429;

  public ApiKeyLimitExceededException(String managerId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("already has 5 or more API keys: %s", managerId)
    );
  }
}
