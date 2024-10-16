package com.goofy.tunabank.v1.exception.user;

import com.goofy.tunabank.v1.exception.CustomException;

public class UserExitException extends CustomException {

  private static final String DEFAULT_MESSAGE = "User has exited";
  private static final String DEFAULT_CODE = "USER_EXITED";
  private static final int DEFAULT_STATUS = 410;  // HTTP 410 Gone

  public UserExitException(String email) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, email)
    );
  }

}
