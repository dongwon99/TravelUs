package com.goofy.tunabank.v1.util;

import java.math.BigDecimal;

public class CurrencyConverter {

  public static BigDecimal convertToKRW(Double foreignAmount, Double exchangeRate) {
    // Double 값을 BigDecimal로 변환
    BigDecimal foreignAmountBD = BigDecimal.valueOf(foreignAmount);
    BigDecimal exchangeRateBD = BigDecimal.valueOf(exchangeRate);

    // 원화로 변환
    BigDecimal krwAmount = foreignAmountBD.multiply(exchangeRateBD);
    return krwAmount;
  }
}
