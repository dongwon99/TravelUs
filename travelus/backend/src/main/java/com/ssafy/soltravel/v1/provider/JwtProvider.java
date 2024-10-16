package com.ssafy.soltravel.v1.provider;

import com.ssafy.soltravel.v1.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  // API KEY 받아오기
  private final Map<String, String> apiKeys;

  // HMAC SHA 알고리즘을 사용하기 위해 바이트 배열로 변환하여 키 객체 생성
  private Key getKey() {
    return Keys.hmacShaKeyFor(apiKeys.get("JWT_SECRET_KEY").getBytes());
  }

  // 토큰 생성 함수
  private String generateToken(int days, Long userId) {
    Date expiredDate = Date.from(Instant.now().plus(days, ChronoUnit.DAYS));
    String jwt = Jwts.builder()
        .signWith(getKey(), SignatureAlgorithm.HS256) // 암호화 알고리즘
        .setSubject(String.valueOf(userId))           // 토큰의 subject 필드에 사용자 ID 설정
        .setIssuedAt(new Date())                      // 토큰 발생 시간
        .setExpiration(expiredDate)                   // 토큰 만료 시간
        .compact();                                   // 토큰을 압축하여 문자열로 반환

    return jwt;
  }

  // AccessToken 생성 -> userId를 받아 JWT 토큰을 생성
  public String generateAccessToken(Long userId) {
    return generateToken(1, userId);
  }

  // RefreshToken 생성 -> 만료 시간 일주일
  public String generagteRefreshToken(Long userId) {
    return generateToken(7, userId);
  }

  // Jwt 유효성 검증 (서명 유효성, 조작 검사, 만료 검사) -> JWT 토큰을 입력받아 검증하고  userId를 반환
  public Long validateToken(String jwt) throws InvalidTokenException, ExpiredJwtException {
    try {
      Claims claims = Jwts.parserBuilder()  // JWT parser 빌더를 사용하여 JWT parser 생성
          .setSigningKey(getKey())          // 서명 키 설정
          .build()                          // 빌더 빌드
          .parseClaimsJws(jwt)              // JWT 파싱 및 검증
          .getBody();                       // JWT의 본문(Claims) 추출
      return Long.valueOf(claims.getSubject());
    } catch (ExpiredJwtException expiredJwtException) {
      throw expiredJwtException;
    } catch (Exception e) {
      throw new InvalidTokenException(jwt);
    }
  }
}
