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
public class AuthSMSSendRequestDto {

    @Schema(description = "사용자의 phone 번호", example = "010-1234-1234")
    String phone;
}
