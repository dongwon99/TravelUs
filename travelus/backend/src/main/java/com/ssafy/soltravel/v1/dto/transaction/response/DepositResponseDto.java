package com.ssafy.soltravel.v1.dto.transaction.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DepositResponseDto {

    @Schema(description = "거래 고유 번호", example = "3526")
    private String transactionUniqueNo;

    @Schema(description = "거래 날짜", example = "20240825")
    private String transactionDate;
}
