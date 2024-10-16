package com.goofy.tunabank.v1.exception.transaction;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidMerchantIdException extends CustomException {

  private static final String DEFAULT_MESSAGE = "merchantId is not valid";
  private static final String DEFAULT_CODE = "INVALID_MERCHANT_ID";
  private static final int DEFAULT_STATUS = 403;

  public InvalidMerchantIdException(Long id){
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s. merchantId: %s", DEFAULT_MESSAGE, id)
    );
  }
}
