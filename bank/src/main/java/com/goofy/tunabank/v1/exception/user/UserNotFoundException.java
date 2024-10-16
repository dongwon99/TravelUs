package com.goofy.tunabank.v1.exception.user;

import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueRequestDto;
import com.goofy.tunabank.v1.exception.CustomException;

public class UserNotFoundException extends CustomException {

  private static final String EMAIL_NOT_REGISTER_MESSAGE = "This email is not registered";
  private static final String EMAIL_NOT_REGISTER_CODE = "EMAIL_NOT_REGISTERED";
  private static final int EMAIL_NOT_REGISTER_STATUS = 401;

  private static final String DEFAULT_MESSAGE = "User not found";
  private static final String DEFAULT_CODE = "USER_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;


  public UserNotFoundException(ApiKeyIssueRequestDto request) {
    super(
        EMAIL_NOT_REGISTER_MESSAGE,
        EMAIL_NOT_REGISTER_CODE,
        EMAIL_NOT_REGISTER_STATUS,
        String.format("%s: %s", EMAIL_NOT_REGISTER_MESSAGE, request.getManagerId())
    );
  }

  public UserNotFoundException(String email) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, email)
    );
  }
}
