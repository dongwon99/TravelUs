package com.ssafy.soltravel.v1.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "exchange_rate")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @Column(name="currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "exchange_min")
    private Double exchangeMin;

    @Column
    private LocalDateTime created;
}
