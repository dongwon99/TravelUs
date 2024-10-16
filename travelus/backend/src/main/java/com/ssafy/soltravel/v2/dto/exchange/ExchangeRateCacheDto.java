package com.ssafy.soltravel.v2.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateCacheDto {

  String CurrencyCode;
  double ExchangeRate;
  String created;
  String exchangeMin;
}
