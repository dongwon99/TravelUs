package com.goofy.tunabank.v1.exception.card;

import com.goofy.tunabank.v1.exception.CustomException;

public class CardOwnershipException extends CustomException {

  private static final String DEFAULT_MESSAGE = "The user does not own this card";
  private static final String DEFAULT_CODE = "CARD_OWNERSHIP_ERROR";
  private static final int DEFAULT_STATUS = 403;

  public CardOwnershipException(String cardNo) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, cardNo)
    );
  }
}

