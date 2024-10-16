package com.ssafy.soltravel.v2.exception.targetrate;

import com.ssafy.soltravel.v2.exception.CustomException;

public class TargetRateNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Target rate not found for the specified ID.";
  private static final String DEFAULT_CODE = "TARGET_RATE_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public TargetRateNotFoundException(Long targetRateId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %d", DEFAULT_MESSAGE, targetRateId)
    );
  }
}
