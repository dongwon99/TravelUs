package com.ssafy.soltravel.v2.exception.settlement;

import com.ssafy.soltravel.v2.exception.CustomException;

public class BillingHistoryDetailNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Billing history detail not found.";
  private static final String DEFAULT_CODE = "BILLING_HISTORY_DETAIL_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public BillingHistoryDetailNotFoundException(Long id) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s : %d", DEFAULT_MESSAGE, id)
    );
  }
}
