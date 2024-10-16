package com.ssafy.soltravel.v2.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthOcrIdCardResponseDto {

  @Schema(description = "주민등록번호", example = "991121-1234567")
  private String residentRegistrationNumber;

  @Schema(description = "이름", example = "허동원")
  private String name;
}
