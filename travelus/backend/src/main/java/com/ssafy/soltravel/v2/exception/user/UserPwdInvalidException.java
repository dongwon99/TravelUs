package com.ssafy.soltravel.v2.exception.user;


import com.ssafy.soltravel.v2.exception.CustomException;

public class UserPwdInvalidException extends CustomException {

  private static final String DEFAULT_MESSAGE = "User password is invalid";
  private static final String DEFAULT_CODE = "USER_PWD_INVALID";
  private static final int DEFAULT_STATUS = 401;  // HTTP 401 Unauthorized

  public UserPwdInvalidException(String email) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, email)
    );
  }
}
