package com.ssafy.soltravel.v2.dto.account_book;

import com.ssafy.soltravel.v2.domain.Enum.OrderByType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistoryReadRequestDto {

  @Schema(description = "조회할 거래의 시작 날짜", example = "2024-08-01")
  private String startDate;

  @Schema(description = "조회할 거래의 종료 날짜", example = "2024-08-31")
  private String endDate;

  @Schema(description = "정렬 기준 (ASC: 오름차순(이전거래), DESC: 내림차순(최근거래))", example = "ASC")
  private OrderByType orderByType;
}
