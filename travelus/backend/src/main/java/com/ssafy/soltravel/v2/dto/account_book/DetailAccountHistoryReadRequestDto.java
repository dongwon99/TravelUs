package com.ssafy.soltravel.v2.dto.account_book;

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
public class DetailAccountHistoryReadRequestDto {

  @Schema(description = "조회할 날짜", example = "2024-10-09")
  private String date;
}
