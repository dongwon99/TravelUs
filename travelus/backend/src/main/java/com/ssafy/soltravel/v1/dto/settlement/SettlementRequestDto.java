package com.ssafy.soltravel.v1.dto.settlement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SettlementRequestDto {

  @Schema(description = "외화 계좌 ID", example = "1")
  private long accountId;

  @Schema(description = "외화 계좌 번호", example = "0883473075115544")
  private String accountNo;
}
