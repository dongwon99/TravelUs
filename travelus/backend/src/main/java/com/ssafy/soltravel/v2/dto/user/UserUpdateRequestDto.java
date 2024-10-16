package com.ssafy.soltravel.v2.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.soltravel.v2.domain.Enum.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank
    @Schema(description = "변경할 이름", example = "홍홍길동")
    private String name;

    @NotBlank
    @Schema(description = "사용자의 전화번호", example = "01062966409")
    private String phone;

    @NotBlank
    @Schema(description = "사용자의 주소", example = "경북 구미시 맥민투")
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "사용자의 생년월일", example = "2990-01-01")
    private LocalDate birth;
}