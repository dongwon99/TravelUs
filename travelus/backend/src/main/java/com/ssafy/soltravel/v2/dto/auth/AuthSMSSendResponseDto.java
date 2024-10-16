package com.ssafy.soltravel.v2.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSMSSendResponseDto {

    @Schema(description = "전화번호", example = "010-1234-1234")
    private String phone;

    @Schema(description = "상태 메시지", example = "SMS 발송 성공")
    private String statusMessage;
}