package com.ssafy.soltravel.v2.dto.transaction.response;

import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionResponseDto {

    @Schema(description = "거래 고유 번호", example = "1")
    private String transactionUniqueNo;

    @Schema(description = "거래 종류", example = "D")
    private TransactionType transactionType;

    @Schema(description = "거래 일시", example = "2024-04-01T10:25:00")
    private String transactionDate;

    @Schema(description = "거래 금액", example = "5000")
    String transactionAmount;

    @Schema(description = "거래 후 계좌 잔액", example = "5000")
    String transactionBalance;

    @Schema(description = "거래 메모", example = "(테스트) : 계좌 입금")
    String transactionSummary;
}
