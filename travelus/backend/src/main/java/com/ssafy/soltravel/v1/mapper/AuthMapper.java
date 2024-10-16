package com.ssafy.soltravel.v1.mapper;

import com.ssafy.soltravel.v1.dto.auth.AuthReissueResponseDto;
import com.ssafy.soltravel.v1.dto.user.UserLoginResponseDto;

public class AuthMapper {

  public static AuthReissueResponseDto convertLoginToReissueDto(UserLoginResponseDto login){
    return AuthReissueResponseDto.builder()
        .accessToken(login.getAccessToken())
        .refreshToken(login.getRefreshToken())
        .build();
  }
}
