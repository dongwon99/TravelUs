package com.ssafy.soltravel.v1.dto.exchange;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponseDto {

    @Schema(description = "환전할 통화 코드", example = "USD")
    private String currencyCode;

    @Schema(description = "실시간 환율", example = "1333.40")
    private Double exchangeRate;

    @Schema(description = "최소 환전 금액", example = "1000.0")
    private Double exchangeMin;
}
