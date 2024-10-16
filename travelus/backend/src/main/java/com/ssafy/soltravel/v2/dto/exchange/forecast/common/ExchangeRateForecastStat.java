package com.ssafy.soltravel.v2.dto.exchange.forecast.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateForecastStat {
  private double average;
  private double min;
  private double max;
  private double p10;
  private double p20;
  private double p30;
  private double p40;
  private double p50;
  private double p60;
  private double p70;
  private double p80;
  private double p90;
  private double p100;
}
