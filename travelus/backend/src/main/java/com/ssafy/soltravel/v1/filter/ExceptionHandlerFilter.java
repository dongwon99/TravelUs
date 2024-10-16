package com.ssafy.soltravel.v1.filter;

import com.ssafy.soltravel.v1.exception.InvalidTokenException;
import com.ssafy.soltravel.v1.exception.UserNotFoundException;
import com.ssafy.soltravel.v1.util.LogUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException expiredJwtException) {
      LogUtil.error(expiredJwtException.getMessage());
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 토큰입니다");

    } catch (InvalidTokenException invalidTokenException) {
      String msg = String.format("유효하지 않은 토큰입니다: %s", invalidTokenException.getToken());
      LogUtil.error(invalidTokenException.getMessage());
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);

    } catch (UserNotFoundException userNotFoundException) {
      String msg = String.format("유효하지 않은 토큰/유저번호입니다: %d", userNotFoundException.getUserId());
      LogUtil.error(userNotFoundException.getMessage());
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);

    } catch (Exception e) {
      LogUtil.error(e.getMessage());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다");
    }
  }
}
