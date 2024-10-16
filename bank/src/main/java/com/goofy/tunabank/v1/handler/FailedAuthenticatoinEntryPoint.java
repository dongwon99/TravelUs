package com.goofy.tunabank.v1.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class FailedAuthenticatoinEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException e
  ) throws IOException {

    // HttpServletRequest에서 AuthException 관련 Info 가져옴
    String code = (String) request.getAttribute("exceptionCode");
    String errorMessage = (String) request.getAttribute("exceptionMsg");

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    code = (code != null) ? (code):("INTERNAL_SERVER_ERROR");
    errorMessage = (errorMessage != null) ? (errorMessage):(e.getMessage());

    String body = String.format(
        "{\"status\": \"Unauthorized\", \"message\": \"%s\", \"errorMessage\": \"%s\"}",
        code, errorMessage
    );

    response.getWriter().write(body);
  }
}

