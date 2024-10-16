package com.ssafy.soltravel.v2.dto.exchange.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.IntegratedForecastResponseData;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllExchangeRateResponseData {

  @JsonProperty("TWD")
  IntegratedForecastResponseData TWD;
  @JsonProperty("USD")
  IntegratedForecastResponseData USD;
  @JsonProperty("EUR")
  IntegratedForecastResponseData EUR;
  @JsonProperty("JPY")
  IntegratedForecastResponseData JPY;

  public void setCurrency(String currencyCode, IntegratedForecastResponseData data) {
    switch (currencyCode) {
      case "EUR" -> EUR = data;
      case "JPY" -> JPY = data;
      case "USD" -> USD = data;
      case "TWD" -> TWD = data;
      default -> throw new IllegalArgumentException("Unknown currency code: " + currencyCode);
    }
  }

}
