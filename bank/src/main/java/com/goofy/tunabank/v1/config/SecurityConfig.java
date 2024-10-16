package com.goofy.tunabank.v1.config;

import com.goofy.tunabank.v1.filter.KeyAutheticationFilter;
import com.goofy.tunabank.v1.handler.FailedAuthenticatoinEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final KeyAutheticationFilter keyAutheticationFilter;
  private final FailedAuthenticatoinEntryPoint failedAuthenticatoinEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())                 //등록된 빈에서 CORS 커스텀 설정 찾아서 등록
        .csrf(AbstractHttpConfigurer::disable)           //csrf 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(requests -> requests      //특정 uri만 허용하고 나머지는 인증받아야함
            .requestMatchers(
                new AntPathRequestMatcher("/api/v1/bank/auth/**"),
                new AntPathRequestMatcher("/api/v1/bank/swagger-ui/**"),
                new AntPathRequestMatcher("/api/v1/bank/v3/api-docs/**"),
                new AntPathRequestMatcher("/api/v1/bank/actuator/health"),
                new AntPathRequestMatcher("/api/v1/bank/exchange/**"),
                new AntPathRequestMatcher("/api/v1/bank/transaction/transfer/moneybox/auto/**"),
                new AntPathRequestMatcher("/api/v1/bank/transaction/settlement/**"),
                new AntPathRequestMatcher("/api/v1/bank/accounts/balance")
            ).permitAll()
            .anyRequest().authenticated()
        ).addFilterBefore(
            keyAutheticationFilter,
            UsernamePasswordAuthenticationFilter.class
        ).exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(failedAuthenticatoinEntryPoint)
        );
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }
}