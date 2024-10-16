package com.ssafy.soltravel.v1.dto.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionRequestDto {

    @Schema(description = "거래 금액", example = "5000")
    Long transactionBalance;

    @Schema(description = "거래 요약", example = "월급 입금")
    String transactionSummary;

    @Schema(description = "유저 ID", example = "1")
    Long userId;
}
