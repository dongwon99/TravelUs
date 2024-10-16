package com.ssafy.soltravel.v1.dto.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignTransactionRequestDto {

  @Schema(description = "입금 금액", example = "1500.50")
  private Double transactionBalance;

  @Schema(description = "거래 요약", example = "환전 입금")
  private String transactionSummary;

  @Schema(description = "유저 ID", example = "1")
  Long userId;
}
