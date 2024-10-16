package com.goofy.tunabank.v1.exception.card;

import com.goofy.tunabank.v1.exception.CustomException;

public class CardExpiredException extends CustomException {

  private static final String DEFAULT_MESSAGE = "The card has expired";
  private static final String DEFAULT_CODE = "CARD_EXPIRED";
  private static final int DEFAULT_STATUS = 403;

  public CardExpiredException(String cardNo) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, cardNo)
    );
  }
}
