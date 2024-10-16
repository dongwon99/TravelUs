package com.goofy.tunabank.v1.exception.user;

import com.goofy.tunabank.v1.exception.CustomException;

public class UserKeyExpiredException extends CustomException {

  private static final String DEFAULT_MESSAGE = "User key has expired";
  private static final String DEFAULT_CODE = "USER_KEY_EXPIRED";
  private static final int DEFAULT_STATUS = 401;

  public UserKeyExpiredException(String email) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s",DEFAULT_MESSAGE, email)
    );
  }

}
