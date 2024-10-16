package com.ssafy.soltravel.v2.dto.user;


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
    @Schema(description = "사용자의 아이디", example = "hdw123", required = true)
    private String id;

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 비밀번호", example = "password123!", required = true)
    private String password;

}
