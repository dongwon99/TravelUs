package com.ssafy.soltravel.v2.exception.exchange;


import com.ssafy.soltravel.v2.exception.CustomException;

public class CurrencyCodeInvalidException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Currency code is invalid";
  private static final String DEFAULT_CODE = "CURRENCY_CODE_INVALID";
  private static final int DEFAULT_STATUS = 400;  // HTTP 400 Bad Request

  public CurrencyCodeInvalidException(String currencyCode) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, currencyCode)
    );
  }

  public CurrencyCodeInvalidException(String message, String code, int status, String currencyCode) {
    super(message, code, status, String.format("%s: %s", message, currencyCode));
  }
}
