package com.ssafy.soltravel.v1.dto.account_book;

import com.ssafy.soltravel.v1.domain.Enum.TransacntionType;
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

  @Schema(description = "조회할 날짜", example = "20240822")
  private String date;

  @Schema(description = "거래 유형 (M:입금, D:출금, A:전체)", example = "A")
  private TransacntionType transactionType;
}
