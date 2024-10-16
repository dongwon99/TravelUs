package com.ssafy.soltravel.v2.dto.account.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteAccountRequestDto {

    @Schema(description = "해지할 계좌 번호", example = "001-2948678-209")
    private String accountNo;

    @Schema(description = "모임 계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "환불 받을 계좌 번호", example = "001-2087533-209")
    private String refundAccountNo;

}
