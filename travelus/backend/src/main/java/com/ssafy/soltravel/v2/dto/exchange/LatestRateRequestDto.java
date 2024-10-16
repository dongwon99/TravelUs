package com.ssafy.soltravel.v2.dto.exchange;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
public class LatestRateRequestDto {

  @Schema(description = "통화 코드 (USD, JPY)", example = "USD")
  private String currencyCode;

  @Schema(description = "환율 조회의 시작 날짜", example = "2024-08-01")
  private LocalDate startDate;

  @Schema(description = "환율 조회의 종료 날짜", example = "2024-08-22")
  private LocalDate endDate;
}
