package com.ssafy.soltravel.v1.config;

import com.ssafy.soltravel.v1.filter.ExceptionHandlerFilter;
import com.ssafy.soltravel.v1.filter.JwtAutheticationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAutheticationFilter jwtAutheticationFilter;
  private final ExceptionHandlerFilter exceptionHandlerFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())                 //등록된 빈에서 CORS 커스텀 설정 찾아서 등록
        .csrf(AbstractHttpConfigurer::disable)           //csrf 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(requests -> requests      //특정 uri만 허용하고 나머지는 인증받아야함
                .requestMatchers(
                    new AntPathRequestMatcher("/api/v1/auth/**"),
                    new AntPathRequestMatcher("/api/v1/user/join"),
                    new AntPathRequestMatcher("/api/v1/user/join/test"),
                    new AntPathRequestMatcher("/api/v1/swagger-ui/**"),
                    new AntPathRequestMatcher("/api/v1/v3/api-docs/**"),
                    new AntPathRequestMatcher("/api/v1/notification/subscribe/**")
                ).permitAll()
                .anyRequest().authenticated()
            //.anyRequest().authenticated()
        ).formLogin(form -> form
            .defaultSuccessUrl("/api/v1/auth/test-ok", true)
            .failureUrl("/api/v1/error")
            .permitAll()
        ).logout(logout -> logout
            .permitAll()
        ).addFilterBefore(
            jwtAutheticationFilter,
            UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
            exceptionHandlerFilter,
            JwtAutheticationFilter.class
        ).exceptionHandling(handle -> handle.authenticationEntryPoint(
            new FailedAuthenticatoinEntryPoint())
        );
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }
}

class FailedAuthenticatoinEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    response.setContentType("application/json"); // 응답 콘텐츠 타입을 JSON으로 설정
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 응답 상태 코드를 401으로 설정
    response.getWriter()
        .write("{\"code\" : \"UA\", \"message\" : \"UnAuthorized.\"}"); // 응답 바디에 JSON 형태로 메시지 작성

  }
}
