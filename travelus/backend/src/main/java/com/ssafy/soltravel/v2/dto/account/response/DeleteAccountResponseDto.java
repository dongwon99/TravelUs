package com.ssafy.soltravel.v2.dto.account.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema
public class DeleteAccountResponseDto {

    @Schema(description = "계좌 삭제 상태", example = "CLOSED")
    private String status;

    @Schema(description = "환불 계좌 번호", example = "110123456789")
    private String refundAccountNo;

    @Schema(description = "환불된 금액", example = "1000000.0")
    private Double refundAmount;

}
