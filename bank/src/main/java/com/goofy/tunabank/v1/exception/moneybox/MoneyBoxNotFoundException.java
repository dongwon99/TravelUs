package com.goofy.tunabank.v1.exception.moneybox;

import com.goofy.tunabank.v1.exception.CustomException;

public class MoneyBoxNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Money box not found for currency ID.";
  private static final String DEFAULT_CODE = "MONEYBOX_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404; // HTTP 404 Not Found

  public MoneyBoxNotFoundException(String currencyCode) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, currencyCode)
    );
  }

  public MoneyBoxNotFoundException(Integer currencyId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %d", DEFAULT_MESSAGE, currencyId)
    );
  }
}
