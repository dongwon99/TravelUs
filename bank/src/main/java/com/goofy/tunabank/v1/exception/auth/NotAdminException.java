package com.goofy.tunabank.v1.exception.auth;

import com.goofy.tunabank.v1.exception.CustomException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class NotAdminException extends CustomException {
  private static final String DEFAULT_MESSAGE = "User is not an admin";
  private static final String DEFAULT_CODE = "USER_NOT_ADMIN";
  private static final int DEFAULT_STATUS = 403;

  public NotAdminException(String managerId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, managerId)
    );
  }
}
