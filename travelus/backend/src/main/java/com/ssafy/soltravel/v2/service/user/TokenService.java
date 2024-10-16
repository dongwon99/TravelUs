package com.ssafy.soltravel.v2.service.user;


import com.ssafy.soltravel.v2.domain.redis.RedisToken;
import com.ssafy.soltravel.v2.dto.user.UserLoginResponseDto;
import com.ssafy.soltravel.v2.provider.JwtProvider;
import com.ssafy.soltravel.v2.repository.redis.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

  private final JwtProvider jwtProvider;
  private final TokenRepository tokenRepository;

  public UserLoginResponseDto saveRefreshToken(Long userId) {
    String accessToken = jwtProvider.generateAccessToken(userId);
    String refreshToken = jwtProvider.generagteRefreshToken(userId);

    tokenRepository.save(new RedisToken(userId, refreshToken));
    return new UserLoginResponseDto(userId, accessToken, refreshToken);
  }

  public Long getUserIdFromRefreshToken(String refreshToken) {
    return jwtProvider.validateToken(refreshToken);
  }

}
