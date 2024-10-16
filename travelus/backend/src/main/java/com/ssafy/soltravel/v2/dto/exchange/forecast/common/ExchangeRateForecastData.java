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
public class ExchangeRateForecastData {

  @JsonProperty("forecast")
  private Map<String, Double> forecast;

  @JsonProperty("current_rate")
  private Double currentRate;

  @JsonProperty("trend")
  private String trend;

  @JsonProperty("forecast_stats")
  private ExchangeRateForecastStat stat;
}
