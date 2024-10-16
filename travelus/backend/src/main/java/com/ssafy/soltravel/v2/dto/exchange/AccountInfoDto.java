package com.ssafy.soltravel.v2.dto.exchange;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDto {

  @Schema(description = "계좌 번호", example = "0883473075115544")
  private String accountNo;

  @Schema(description = "계좌 ID", example = "1")
  private Long accountId;

  @Schema(description = "환전된 금액", example = "0.75")
  private double amount;

  @Schema(description = "계좌 잔액", example = "1500000")
  private double balance;
}
