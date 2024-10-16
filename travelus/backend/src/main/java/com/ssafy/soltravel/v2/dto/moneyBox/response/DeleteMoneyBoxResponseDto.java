package com.ssafy.soltravel.v2.dto.moneyBox.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteMoneyBoxResponseDto {

    @Schema(description = "계좌 상태", example = "CLOSED")
    private String status;

    @Schema(description = "머니박스 삭제할 계좌 번호", example = "001-23464563-209")
    private String refundAccountNo;

    @Schema(description = "환불 받은 금액 (외화 -> KRW)", example = "100000.0")
    private Double refundAmount;

}

