package com.ssafy.soltravel.v2.exception.moneybox;

import com.ssafy.soltravel.v2.exception.CustomException;

public class MoneyBoxNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Money box not found for currency ID.";
  private static final String DEFAULT_CODE = "MONEYBOX_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public MoneyBoxNotFoundException(String currencyId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, currencyId)
    );
  }
}