package com.ssafy.soltravel.v1.dto.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransferRequestDto {

    @Schema(description = "입금할 계좌 번호", example = "0887850232491646")
    private String depositAccountNo;

    @Schema(description = "입금 거래 요약", example = "입금 완료")
    private String depositTransactionSummary;

    @Schema(description = "거래 금액", example = "1000000")
    private Long transactionBalance;

    @Schema(description = "출금 거래 요약", example = "출금 완료")
    private String withdrawalTransactionSummary;
}
