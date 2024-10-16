package com.ssafy.soltravel.v2.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CardUsageAmountResponseDto {

  @Schema(description = "원화 사용 금액", example = "10000")
  private Double krwAmount;

  @Schema(description = "외화 사용 금액", example = "20")
  private Double foreignAmount;
}
