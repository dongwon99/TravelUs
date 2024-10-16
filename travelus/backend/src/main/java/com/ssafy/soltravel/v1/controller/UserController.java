package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.ResponseDto;
import com.ssafy.soltravel.v1.dto.user.EmailValidationDto;
import com.ssafy.soltravel.v1.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.v1.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.v1.dto.user.UserSearchResponseDto;
import com.ssafy.soltravel.v1.service.account.AccountService;
import com.ssafy.soltravel.v1.service.user.UserService;
import com.ssafy.soltravel.v1.util.LogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value="/join", consumes = "multipart/form-data")
    public ResponseEntity<?> createUser(@ModelAttribute UserCreateRequestDto joinDto)
        throws IOException {

        LogUtil.info("requested", joinDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(joinDto));
    }

    @Operation(summary = "회원가입 테스트", description = "새로운 사용자를 등록합니다.(신한 api 사용하지 않는 버전)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value="/join/test", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDto> createUserWithoutAPI(@ModelAttribute UserCreateRequestDto joinDto)
        throws IOException {

        LogUtil.info("requested", joinDto.toString());
        userService.createUserWithoutAPI(joinDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
    }

    @Operation(summary = "단일 사용자 조회", description = "사용자 ID로 단일 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserSearchResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/search/{userId}")
    public ResponseEntity<?> searchUser(@PathVariable(name = "userId") Long userId) {

        LogUtil.info("requested", userId);
        UserSearchResponseDto response = userService.searchOneUser(userId);
        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "모든 사용자 조회", description = "모든 사용자를 조건에 맞춰 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserSearchResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/search/all")
    public ResponseEntity<?> searchAllUser(@ModelAttribute UserSearchRequestDto searchDto) {

        LogUtil.info("requested", searchDto.toString());
        List<UserSearchResponseDto> response = userService.searchAllUser(searchDto);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "모임원 이메일 유효성 검사", description = "이메일을 통해 회원인지 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EmailValidationDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/validate-email/{email}")
    public ResponseEntity<EmailValidationDto> validateEmail(@PathVariable String email) {

        return ResponseEntity.ok().body(accountService.getPersonalAccountByEmail(email));
    }
}
