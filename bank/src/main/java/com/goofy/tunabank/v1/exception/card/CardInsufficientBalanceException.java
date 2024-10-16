package com.goofy.tunabank.v1.exception.card;

import com.goofy.tunabank.v1.exception.CustomException;

public class CardInsufficientBalanceException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Insufficient balance on card.";
  private static final String DEFAULT_CODE = "INSUFFICIENT_BALANCE";
  private static final int DEFAULT_STATUS = 400;

  public CardInsufficientBalanceException(String cardNo, double requestedAmount, double availableBalance) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s (CardNo: %s, Requested: %.2f, Available: %.2f)",
            DEFAULT_MESSAGE, cardNo, requestedAmount, availableBalance)
    );
  }
}
