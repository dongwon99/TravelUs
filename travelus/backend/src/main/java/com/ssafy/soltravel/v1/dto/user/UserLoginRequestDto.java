package com.ssafy.soltravel.v1.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDto {

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 이메일 주소", example = "user@google.com", required = true)
    private String email;

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 비밀번호", example = "password123!", required = true)
    private String password;

}
