package com.ssafy.soltravel.v1.dto.transaction.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransferHistoryResponseDto {

    @Schema(description = "거래 고유 번호", example = "20230819123012345678")
    String transactionUniqueNo;

    @Schema(description = "계좌 번호", example = "0889876543210")
    String accountNo;

    @Schema(description = "거래 날짜", example = "2023-08-19T12:30:00")
    String transactionDate;

    @Schema(description = "거래 유형 코드", example = "DEPOSIT")
    String transactionType;

    @Schema(description = "거래 유형 이름", example = "입금")
    String transactionTypeName;

    @Schema(description = "거래 상대방 계좌 번호", example = "0881234567890")
    String transactionAccountNo;
}
