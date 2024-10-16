package com.ssafy.soltravel.v2.domain;


import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
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
    name = "exchange_rate",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = {"date", "currency"})
    }
)
public class ExchangeRate {

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
  private Double rate;

  public static ExchangeRate create(LocalDate date, CurrencyType currency, Double rate) {
    ExchangeRate forecast = new ExchangeRate();
    forecast.date = date;
    forecast.currency = currency;
    forecast.rate = rate;
    return forecast;
  }

  public void updateRate(Double newRate) {
    this.rate = newRate;
  }
}
