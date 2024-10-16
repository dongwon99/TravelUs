package com.ssafy.soltravel.v2.dto.exchange.forecast;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.ExchangeRateData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.ExchangeRateForecastData;
import java.time.LocalDateTime;
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
public class ExchangeRateForecastSaveRequestDto {
  private Map<String, ExchangeRateForecastData> currencies = new HashMap<>();

  @JsonProperty("last_updated")
  private LocalDateTime lastUpdated;

  @JsonAnySetter
  public void setCurrency(String currencyCode, ExchangeRateForecastData forecastData) {
    this.currencies.put(currencyCode, forecastData);
  }
}
