package com.ssafy.soltravel.v2.dto.exchange.forecast.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.domain.Enum.Trend;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntegratedForecastResponseData {

  private Map<String, Double> forecast;

  private Trend trend;

  @JsonProperty("current_rate")
  private Double currentRate;

  @JsonProperty("forecast_stats")
  private ExchangeRateForecastStat stat;

  @JsonProperty("recent_rates")
  private Map<String, Map<String, Double>> recentRates;
}
