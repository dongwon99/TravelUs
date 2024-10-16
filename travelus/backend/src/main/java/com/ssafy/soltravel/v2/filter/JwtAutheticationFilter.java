package com.ssafy.soltravel.v2.filter;

import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.exception.auth.InvalidTokenException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.provider.JwtProvider;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAutheticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      //토큰 파싱
      String token = parseBearerToken(request);
      if (token == null) {
        throw new InvalidTokenException("NULL");
      }

      //유효성 검사
      Long userId = jwtProvider.validateToken(token);
      if (userId == null) {
        throw new InvalidTokenException(token);
      }

      // 추출한 user id로 UserEntity 조회
      User user = userRepository.findByUserId(userId).orElseThrow(
          () -> new UserNotFoundException(userId)
      );
      String role = String.valueOf(user.getRole());

      // 사용자 역할을 GrantedAuthority 리스트(여러개 role 가질수도 있으므로)에 추가
      // ROLE_DEVELOPER, ROLE_MASTER ... 규칙 따라 role 규정
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(role));

      // 새로운 SecurityContext 생성
      SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

      // 사용자 ID와 권한으로 UsernamePasswordAuthenticationToken 생성
      AbstractAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(userId, null, authorities);

      // 요청의 세부 정보를 설정
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      // SecurityContext에 인증 토큰 설정
      securityContext.setAuthentication(authenticationToken);

      // SecurityContextHolder에 SecurityContext 설정
      SecurityContextHolder.setContext(securityContext);

    } catch (ExpiredJwtException e) {
      LogUtil.error("Expired JWT token");

    } catch (InvalidTokenException e) {
      LogUtil.warn("Invalid JWT token", e.getToken());

    } catch (UserNotFoundException e) {
      LogUtil.error("User Not Exist", e.getUserId());

    } finally {
      filterChain.doFilter(request, response);
    }
  }

  private String parseBearerToken(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");

    // 실제 값이 있는지 검증 -> null, 길이 0, 공백으로만 : false , 아니면 true
    if (!StringUtils.hasText(authorization)) {
      return null;
    }

    // 문자열이 'Bearer ' 로 시작되는지 체크 -> 정상적인 Bearer 토큰인지 검증
    if (!authorization.startsWith("Bearer ")) {
      return null;
    }

    return authorization.substring(7);
  }
}
