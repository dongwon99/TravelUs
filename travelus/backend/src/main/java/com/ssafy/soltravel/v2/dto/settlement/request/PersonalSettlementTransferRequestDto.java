package com.ssafy.soltravel.v2.dto.settlement.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PersonalSettlementTransferRequestDto {

  @Schema(description = "개별 지출 정산 내역 id",example = "1")
  private Long settlementDetailId;

    @Schema(description = "출금 계좌번호", example = "001-69042343-209")
    private String withdrawalAccountNo;

    @Schema(description = "입금 계좌번호", example = "001-69042343-209")
    private String depositAccountNo;

    @Schema(description = "거래 금액", example = "5000")
    private Double transactionBalance;

    @Schema(description = "출금 거래 요약", example = "이체 출금 테스트")
    private String withdrawalTransactionSummary;

    @Schema(description = "출금 계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "입금 거래 요약", example = "이체 입금 테스트")
    private String depositTransactionSummary;
}
