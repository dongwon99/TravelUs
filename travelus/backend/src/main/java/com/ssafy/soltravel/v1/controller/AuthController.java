package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.auth.AuthReissueRequestDto;
import com.ssafy.soltravel.v1.dto.auth.AuthReissueResponseDto;
import com.ssafy.soltravel.v1.dto.auth.AuthSMSSendRequestDto;
import com.ssafy.soltravel.v1.dto.auth.AuthSMSSendResponseDto;
import com.ssafy.soltravel.v1.dto.auth.AuthSMSVerificationRequestDto;
import com.ssafy.soltravel.v1.dto.auth.AuthSMSVerificationResponseDto;
import com.ssafy.soltravel.v1.dto.user.UserLoginRequestDto;
import com.ssafy.soltravel.v1.dto.user.UserLoginResponseDto;
import com.ssafy.soltravel.v1.service.user.AuthService;
import com.ssafy.soltravel.v1.util.LogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "Authentication", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "일반 회원 로그인", description = "일반 사용자의 로그인 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = UserLoginResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody UserLoginRequestDto loginRequestDto
    ) {
        LogUtil.info(loginRequestDto.toString());
        UserLoginResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "SMS 인증 코드 전송", description = "휴대폰 번호로 인증 코드를 포함한 문자를 전송합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "SMS 전송 성공", content = @Content(schema = @Schema(implementation = AuthSMSSendResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/verify/phone/send")
    public ResponseEntity<?> sendSMSVerification(
        @RequestBody AuthSMSSendRequestDto sendRequestDto
    ) {

        LogUtil.info("requested", sendRequestDto.toString());
        return ResponseEntity.ok().body(authService.sendSMSForVerification(sendRequestDto));
    }


    @Operation(summary = "SMS 코드 인증", description = "사용자가 전송받은 SMS 인증 코드를 검증합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "인증 성공", content = @Content(schema = @Schema(implementation = AuthSMSVerificationResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 인증 코드", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/verify/phone/code")
    public ResponseEntity<AuthSMSVerificationResponseDto> codeVerification(
        @RequestBody AuthSMSVerificationRequestDto verifyRequestDto
    ) {

        LogUtil.info("requested", verifyRequestDto.toString());
        return ResponseEntity.ok().body(authService.verifySMSAuthCode(verifyRequestDto));
    }


  @Operation(summary = "토큰 재발급", description = "RefreshToken을 이용하여 AccessToken을 재발급합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "401", description = "잘못된 refresh token", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(@RequestBody AuthReissueRequestDto reissueRequestDto){

    LogUtil.info("requested", reissueRequestDto.toString());
    AuthReissueResponseDto response = authService.reissueRefreshToken(reissueRequestDto);
    return ResponseEntity.ok().body(response);
  }




  @Operation(hidden = true)
  @GetMapping("/test")
  public String test() {
    return "test";
  }

    @Operation(hidden = true)
    @GetMapping("/test-ok")
    public String testOk() {
        return "testOk";
    }
}
