package com.ssafy.soltravel.v2.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPwdUpdateRequestDto {

    @NotBlank
    @Schema(description = "변경 전 비밀번호(인증용)", example = "password123!")
    private String before;

    @NotBlank
    @Schema(description = "변경 후 비밀번호", example = "123456")
    private String after;
}