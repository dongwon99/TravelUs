package com.goofy.tunabank.v1.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ExchangeRateCacheDTO {

  String CurrencyCode;
  double ExchangeRate;
  String created;
  int exchangeMin;
}
