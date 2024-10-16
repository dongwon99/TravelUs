package com.goofy.tunabank.v1.exception.exchange;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.exception.CustomException;

public class UnsupportedCurrencyException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Unsupported currency type";
  private static final String DEFAULT_CODE = "UNSUPPORTED_CURRENCY";
  private static final int DEFAULT_STATUS = 400;

  public UnsupportedCurrencyException(CurrencyType currencyCode) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, currencyCode)
    );
  }
}
