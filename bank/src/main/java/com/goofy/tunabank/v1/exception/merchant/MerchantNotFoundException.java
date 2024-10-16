package com.goofy.tunabank.v1.exception.merchant;

import com.goofy.tunabank.v1.exception.CustomException;

public class MerchantNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Merchant not found for the given ID.";
  private static final String DEFAULT_CODE = "MERCHANT_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public MerchantNotFoundException(String merchantId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, merchantId)
    );
  }
}
