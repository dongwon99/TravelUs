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
public class AuthSMSVerificationRequestDto {

    @Schema(description = "전화번호", example = "010-1234-1234")
    private String phone;

    @Schema(description = "인증 코드", example = "123456")
    private String authCode;
}
