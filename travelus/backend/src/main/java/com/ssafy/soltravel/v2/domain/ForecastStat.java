package com.ssafy.soltravel.v2.domain;


import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.Trend;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.ExchangeRateForecastStat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;

@Entity
@Getter
@Table(
    name = "forecast_stat",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = {"date", "currency"})
    }
)
public class ForecastStat {
  @Id
  @Column(name = "exchange_rate_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CurrencyType currency;

  @Column
  @Enumerated(EnumType.STRING)
  private Trend trend;

  @Column
  private Double average;

  @Column
  private Double min;

  @Column
  private Double max;

  @Column
  private Double p10;

  @Column
  private Double p20;

  @Column
  private Double p30;

  @Column
  private Double p40;

  @Column
  private Double p50;

  @Column
  private Double p60;

  @Column
  private Double p70;

  @Column
  private Double p80;

  @Column
  private Double p90;

  @Column
  private Double p100;

  public static ForecastStat createForecastStat(
      CurrencyType currency,
      Trend trend,
      Double... values // 가변 인자로 선택 매개변수 수신
  ) {
    ForecastStat forecastStat = new ForecastStat();

    // 필수 매개변수 설정
    forecastStat.date = LocalDate.now();
    forecastStat.currency = currency;
    forecastStat.trend = trend;

    if(values.length != 13) return null;

    forecastStat.average = values[0];
    forecastStat.min = values[1];
    forecastStat.max = values[2];
    forecastStat.p10 = values[3];
    forecastStat.p20 = values[4];
    forecastStat.p30 = values[5];
    forecastStat.p40 = values[6];
    forecastStat.p50 = values[7];
    forecastStat.p60 = values[8];
    forecastStat.p70 = values[9];
    forecastStat.p80 = values[10];
    forecastStat.p90 = values[11];
    forecastStat.p100 = values[12];

    return forecastStat;
  }

  public static ForecastStat createForecastStat(
      CurrencyType currency,
      String trend,
      ExchangeRateForecastStat stat
  ) {
    ForecastStat forecastStat = new ForecastStat();

    // 필수 매개변수 설정
    forecastStat.date = LocalDate.now();
    forecastStat.currency = currency;
    forecastStat.trend = Trend.valueOf(trend.toUpperCase());

    forecastStat.average = stat.getAverage();
    forecastStat.min = stat.getMin();
    forecastStat.max = stat.getMax();
    forecastStat.p10 = stat.getP10();
    forecastStat.p20 = stat.getP20();
    forecastStat.p30 = stat.getP30();
    forecastStat.p40 = stat.getP40();
    forecastStat.p50 = stat.getP50();
    forecastStat.p60 = stat.getP60();
    forecastStat.p70 = stat.getP70();
    forecastStat.p80 = stat.getP80();
    forecastStat.p90 = stat.getP90();
    forecastStat.p100 = stat.getP100();
    return forecastStat;
  }

}
