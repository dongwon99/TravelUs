package com.ssafy.soltravel.v2.dto.exchange;

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

    @Schema(description = "최소 환전 금액", example = "100")
    private String exchangeMin;

    @Schema(description = "업데이트 시간", example="2024-09-22 00:00")
    private String created;
}
