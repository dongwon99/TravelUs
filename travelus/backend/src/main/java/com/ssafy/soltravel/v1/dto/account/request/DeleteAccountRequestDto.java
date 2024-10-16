package com.ssafy.soltravel.v1.dto.account.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteAccountRequestDto {

    @Schema(description = "환불 받을 계좌 번호", example = "0885486543210")
    private String refundAccountNo;
}
