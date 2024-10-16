package com.goofy.tunabank.v1.exception.card;

import com.goofy.tunabank.v1.exception.CustomException;

public class CardNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "The card does not exist";
  private static final String DEFAULT_CODE = "CARD_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public CardNotFoundException(String cardNo) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, cardNo)
    );
  }
}
