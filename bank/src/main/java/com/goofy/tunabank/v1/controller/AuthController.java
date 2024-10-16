package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueReponseDto;
import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueRequestDto;
import com.goofy.tunabank.v1.service.AuthService;
import com.goofy.tunabank.v1.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/issue/api-key")
  public ResponseEntity issueApiKey(@RequestBody ApiKeyIssueRequestDto request) {
    LogUtil.info("api 발급 요청 수신", request.getManagerId());
    ApiKeyIssueReponseDto response = authService.issueApiKey(request);
    return new ResponseEntity(response, HttpStatus.CREATED);
  }
}
