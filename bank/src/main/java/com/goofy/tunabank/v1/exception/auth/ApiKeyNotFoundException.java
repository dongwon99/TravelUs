package com.goofy.tunabank.v1.exception.auth;

public class ApiKeyNotFoundException extends AuthException {
  private static final String DEFAULT_MESSAGE = "Api Key does not exist";
  private static final String DEFAULT_CODE = "API_KEY_NOT_FOUND";
  private static final int DEFAULT_STATUS = 401;

  public ApiKeyNotFoundException(String apiKey) {
    super(
        DEFAULT_MESSAGE,
        apiKey,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, apiKey)
    );
  }

  public ApiKeyNotFoundException(String message, String apiKey) {
    super(
        message,
        apiKey,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", message, apiKey)
    );
  }
}
