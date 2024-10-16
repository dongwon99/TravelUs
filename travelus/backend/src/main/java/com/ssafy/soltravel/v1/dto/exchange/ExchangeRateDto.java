package com.ssafy.soltravel.v1.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDto {

    private int id;
    private String currency;
    private String exchangeRate;
    private String exchangeMin;
    private String created;
}
