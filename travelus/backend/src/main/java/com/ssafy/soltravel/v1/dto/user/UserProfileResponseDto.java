package com.ssafy.soltravel.v1.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "사용자 프로필 응답 DTO")
public class UserProfileResponseDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 이름", example = "SolTravel")
    private String username;

    @Schema(description = "사용자 이메일", example = "SolTravel@example.com")
    private String email;

    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "사용자 주소", example = "서울특별시 강남구")
    private String address;

    @Schema(description = "사용자 생년월일", example = "1990-01-01")
    private LocalDate birth;

    @Schema(description = "사용자 가입일시", example = "2023-08-26T14:30:00")
    private LocalDateTime registerAt;

}
