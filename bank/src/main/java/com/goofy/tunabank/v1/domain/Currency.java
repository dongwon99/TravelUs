package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Currency {

    @Id
    @Column(name = "currency_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code")
    private CurrencyType currencyCode;  // KOR, JPY, USD 등

    private double exchangeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    Country country;

    //수정 일시
    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name="exchange_min")
    private Integer exchangeMin;
}
