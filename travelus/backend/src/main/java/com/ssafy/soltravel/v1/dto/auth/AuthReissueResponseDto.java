package com.ssafy.soltravel.v1.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthReissueResponseDto {
  private String accessToken;
  private String refreshToken;
}
