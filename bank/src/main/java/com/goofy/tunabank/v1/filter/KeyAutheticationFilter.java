package com.goofy.tunabank.v1.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.exception.auth.ApiKeyNotFoundException;
import com.goofy.tunabank.v1.exception.auth.AuthException;
import com.goofy.tunabank.v1.provider.KeyProvider;
import com.goofy.tunabank.v1.repository.KeyRepository;
import com.goofy.tunabank.v1.repository.UserRepository;
import com.goofy.tunabank.v1.service.UserService;
import com.goofy.tunabank.v1.util.CachedBodyHttpServletRequest;
import com.goofy.tunabank.v1.util.LogUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class KeyAutheticationFilter extends OncePerRequestFilter {

  private final static String REQUEST_HEADER = "Header";
  private final static String REQUEST_APIKEY = "apiKey";
  private final static String REQUEST_USERKEY = "userKey";

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final UserService userService;
  private final UserRepository userRepository;
  private final KeyRepository keyRepository;
  private final KeyProvider keyProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    // CachedBodyHttpServletRequest로 요청 본문 캐싱
    CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

    try {
      // api key 파싱
      String apiKey = resolveHeader(cachedRequest).orElseThrow(
          () -> new ApiKeyNotFoundException("Api Key is missing", "NULL")
      );

      // api key 조회 및 존재 유무 검사
      Key key = keyRepository.findByKeyValue(apiKey).orElseThrow(
          () -> new ApiKeyNotFoundException(apiKey)
      );

      // api key 유효성 검사 & 인증 정보 설정
      if (keyProvider.validateKey(key)) {

        // 유저키 파싱
        String userKey = resolveUser(cachedRequest).orElse(null);
        
        // 저장할 인증 정보 생성
        Authentication auth = getAuthentication(userKey);
        
        // 저장
        SecurityContextHolder.getContext().setAuthentication(auth);
      }

    } catch (AuthException e) {
      LogUtil.warn(e.getInfo());
      cachedRequest.setAttribute("exceptionCode", e.getCode());
      cachedRequest.setAttribute("exceptionMsg", e.getMessage());  // cachedRequest에 예외 설정

    } catch (Exception e) {
      e.printStackTrace();
      cachedRequest.setAttribute("exceptionCode", "INTERNAL_SERVER_ERROR");
      cachedRequest.setAttribute("exceptionMsg", e.getMessage());  // cachedRequest에 예외 설정

    } finally {
      filterChain.doFilter(cachedRequest, response);
    }
  }

  // 요청(헤더) 파싱하기
  private Optional<String> resolveHeader(HttpServletRequest request) throws IOException {
    String requestBody = request.getReader().lines().collect(Collectors.joining());
    Map<String, Object> bodyMap = objectMapper.readValue(requestBody, Map.class);

    // 헤더를 Optional로 처리하여 값이 없을 경우 빈 Optional 반환
    return Optional.ofNullable(
            (Map<String, String>) bodyMap.get(REQUEST_HEADER))
        .map(headerMap -> headerMap.get(REQUEST_APIKEY))
        .filter(apiKey -> apiKey != null && !apiKey.isEmpty()
        );
  }

  // 요청(userKey) 파싱하기
  private Optional<String> resolveUser(HttpServletRequest request) throws IOException {
    String requestBody = request.getReader().lines().collect(Collectors.joining());
    Map<String, Object> bodyMap = objectMapper.readValue(requestBody, Map.class);

    // 헤더를 Optional로 처리하여 값이 없을 경우 빈 Optional 반환
    return Optional.ofNullable(
            (Map<String, String>) bodyMap.get(REQUEST_HEADER))
        .map(headerMap -> headerMap.get(REQUEST_USERKEY))
        .filter(apiKey -> apiKey != null && !apiKey.isEmpty()
        );
  }

  // 인증 정보 생성하기
  private Authentication getAuthentication(String userKey) {
    String effectiveUserKey = (userKey != null) ? userKey : "0";
    UserDetails userDetails = makeUserDetails(effectiveUserKey, "", "USER");
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // 유저 정보 생성하기
  private UserDetails makeUserDetails(String username, String password, String role) {
    return new org.springframework.security.core.userdetails.User(
        username,
        "",
        Collections.singletonList(new SimpleGrantedAuthority(role))
    );
  }
}
