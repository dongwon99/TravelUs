package com.goofy.tunabank.v1.exception.auth;

public class InvalidApiKeyException extends AuthException {

  private static final String DEFAULT_MESSAGE = "Invalid Api Key";
  private static final String DEFAULT_CODE = "API_KEY_INVALID";
  private static final int DEFAULT_STATUS = 401;


  public InvalidApiKeyException(String apiKey) {
    super(
        DEFAULT_MESSAGE,
        apiKey,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, apiKey)
    );
  }
}
