package com.ssafy.soltravel.v1.dto.account_book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistorySaveResponseDto {

  @Schema(description = "응답 메세지", example = "가계부가 등록되었습니다.")
  private String message;

}
