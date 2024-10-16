package com.goofy.tunabank.v1.exception.auth;

import java.time.LocalDateTime;

public class ApiKeyExpiredException extends AuthException {

  private static final String DEFAULT_MESSAGE = "Api Key expired at: ";
  private static final String DEFAULT_CODE = "API_KEY_EXPIRED";
  private static final int DEFAULT_STATUS = 401;


  public ApiKeyExpiredException(String apiKey, LocalDateTime expireAt) {
    super(
        String.format("%s: %s", DEFAULT_MESSAGE, expireAt.toLocalDate()),
        apiKey,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, expireAt.toLocalDate())
    );
  }
}
