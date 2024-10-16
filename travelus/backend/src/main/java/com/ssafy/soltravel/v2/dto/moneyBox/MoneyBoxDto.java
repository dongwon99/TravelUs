package com.ssafy.soltravel.v2.dto.moneyBox;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBoxDto {

    @Schema(description = "머니박스의 고유 ID", example = "1")
    private Long moneyBoxId;

    @Schema(description = "머니박스의 잔액", example = "1000.50")
    private double balance;

    @Schema(description = "통화 코드 (예: 'KRW', 'USD')", example = "USD")
    private CurrencyType currencyCode;
}
