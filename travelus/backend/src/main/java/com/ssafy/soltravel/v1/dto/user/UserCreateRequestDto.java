package com.ssafy.soltravel.v1.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {

    @Schema(description = "사용자의 프로필 이미지", example = "file")
    MultipartFile file;

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 이름", example = "홍길동")
    private String name;

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 이메일 주소", example = "hong@google.com")
    private String email;

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 비밀번호", example = "password123!")
    private String password;

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 전화번호", example = "01012345678")
    private String phone;

    @NotNull
    @NotBlank
    @Schema(description = "사용자의 주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "사용자의 생년월일", example = "1990-01-01")
    private LocalDate birth;

    @NotNull
    @NotBlank
    @Schema(description = "새로 생성할 계좌 비밀번호", example = "1234")
    private String accountPwd;
}