package com.ssafy.soltravel.v1.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSMSVerificationResponseDto {

    @Schema(description = "전화번호", example = "+821012345678")
    private String phone;

    @Schema(description = "상태 메시지", example = "인증 성공")
    private String statusMessage;
}
