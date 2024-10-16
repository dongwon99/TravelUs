package com.goofy.tunabank.v1.exception.card;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidCvcException extends CustomException {

  private static final String DEFAULT_MESSAGE = "The provided CVC number is incorrect";
  private static final String DEFAULT_CODE = "INVALID_CVC";
  private static final int DEFAULT_STATUS = 400;

  public InvalidCvcException(String cardNo) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s for card: %s", DEFAULT_MESSAGE, cardNo)
    );
  }
}
