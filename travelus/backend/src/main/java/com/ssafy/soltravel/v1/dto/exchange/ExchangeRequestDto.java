package com.ssafy.soltravel.v1.dto.exchange;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequestDto {

  @Schema(example = "1", description = "모임주 ID")
  private long userId;

  @Schema(example = "1", description = "모임 계좌 ID")
  private long accountId;

  @Schema(example = "0883473075115544", description = "모임 계좌 번호")
  private String accountNo;

  @Schema(example = "USD", description = "환전할 외화 통화 코드")
  private String currencyCode;

  @Schema(example = "1000", description = "환전할 원화 금액")
  private double exchangeAmount;

  @Schema(example = "1333.40", description = "환율")
  private Double exchangeRate;
}
