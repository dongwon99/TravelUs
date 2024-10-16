package com.ssafy.soltravel.v2.dto.exchange.forecast.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateData {

  @JsonProperty("forecast")
  private Map<String, Double> forecast;

  @JsonProperty("average_forecast")
  private Double averageForecast;

  @JsonProperty("confidence_interval")
  private Map<String, Double> confidenceInterval;

  @JsonProperty("recent_rates")
  private Map<String, Object> recentRates;
}