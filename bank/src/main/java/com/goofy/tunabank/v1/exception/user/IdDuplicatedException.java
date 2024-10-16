package com.goofy.tunabank.v1.exception.user;

import com.goofy.tunabank.v1.exception.CustomException;

public class IdDuplicatedException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Email is already registered";
  private static final String DEFAULT_CODE = "EMAIL_DUPLICATED";
  private static final int DEFAULT_STATUS = 409;  // HTTP 409 Conflict

  public IdDuplicatedException(String email) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, email)
    );
  }
}
