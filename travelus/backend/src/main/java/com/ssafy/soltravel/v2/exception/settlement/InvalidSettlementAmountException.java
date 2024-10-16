package com.ssafy.soltravel.v2.exception.settlement;

import com.ssafy.soltravel.v2.exception.CustomException;
import java.math.BigDecimal;

public class InvalidSettlementAmountException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Invalid settlement amount.";
  private static final String DEFAULT_CODE = "INVALID_SETTLEMENT_AMOUNT";
  private static final int DEFAULT_STATUS = 400;

  public InvalidSettlementAmountException(double amount) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s [Amount: %s]", DEFAULT_MESSAGE, amount)
    );
  }
}
