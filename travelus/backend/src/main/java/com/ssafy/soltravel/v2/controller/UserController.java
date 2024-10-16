package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.user.EmailValidationDto;
import com.ssafy.soltravel.v2.dto.user.ProfileUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserDupCheckRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserPwdUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserSearchResponseDto;
import com.ssafy.soltravel.v2.dto.user.UserUpdateRequestDto;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.user.UserService;
import com.ssafy.soltravel.v2.util.LogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;

    @Operation(summary = "아이디 중복검사", description = "회원가입 전 아이디를 검사합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "중복된 아이디가 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value = "/dup-check")
    public ResponseEntity<?> createUser(
        @Schema(description = "중복 검사를 수행할 사용자 아이디", required = true, example = "중복검사 할 아이디, 쌍따옴표는 빼주세요")
        @RequestBody UserDupCheckRequestDto request ) throws IOException {

        LogUtil.info("requested", request.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userService.checkDupUser(request.getId()));
    }


    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value = "/join", consumes = "multipart/form-data")
    public ResponseEntity<?> createUser(@ModelAttribute UserCreateRequestDto joinDto)
        throws IOException {

        LogUtil.info("requested", joinDto.toString());
        userService.createUser(joinDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
    }


    @Operation(summary = "회원가입 테스트", description = "새로운 사용자를 등록합니다.(은행 api 사용하지 않는 버전)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value = "/join/test", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDto> createUserWithoutAPI(@ModelAttribute UserCreateRequestDto joinDto)
        throws IOException {

        LogUtil.info("requested", joinDto.toString());
        userService.createUserWithoutAPI(joinDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
    }


    @Operation(summary = "단일 사용자 조회", description = "토큰으로 단일 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserSearchResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchUser() {

        LogUtil.info("requested");
        UserSearchResponseDto response = userService.searchOneUser();
        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "전체 사용자 조회(검색)", description = "모든 사용자를 조건에 맞춰 조회합니다. \n검색 기능을 구현해놨으므로, 아래 값 중에 검색하고자 하는 값만 넣고 요청보내면 됩니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserSearchResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value = "/search/all", produces = "application/json")
    public ResponseEntity<?> searchAllUser(@RequestBody(required = false) UserSearchRequestDto searchDto) {

        LogUtil.info("requested", searchDto);
        List<UserSearchResponseDto> response = userService.searchAllUser(searchDto);
        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "프로필 이미지 변경", description = "사진을 업로드해 프로필 이미지를 변경합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "변경 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(name = "/update/profile", consumes = "multipart/form-data")
    public ResponseEntity<?> updateUserProfile(@ModelAttribute ProfileUpdateRequestDto request) throws IOException {

        LogUtil.info("requested", request.toString());
        userService.updateUserProfile(request);
        return ResponseEntity.ok().body(new ResponseDto());
    }

    @Operation(summary = "유저 정보 수정", description = "유저 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "정보 수정 완료", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDto> updateUser(@ModelAttribute UserUpdateRequestDto request)
        throws IOException {

        LogUtil.info("requested", request.toString());
        String message = String.format("갱신이 완료되었습니다: %s", userService.updateUser(request));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(message));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 완료", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping(value = "/update/password")
    public ResponseEntity<ResponseDto> updatePwd(@RequestBody UserPwdUpdateRequestDto request)
        throws IOException {

        LogUtil.info("requested", request.toString());
        String message = String.format("비밀번호가 변경되었습니다: %s", userService.updatePwdrequest(request));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(message));
    }


//
//    @Operation(summary = "모임원 이메일 유효성 검사", description = "이메일을 통해 회원인지 확인합니다.")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = EmailValidationDto.class))),
//        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
//        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @GetMapping("/validate-email/{email}")
//    public ResponseEntity<EmailValidationDto> validateEmail(@PathVariable String email) {
//
//        return ResponseEntity.ok().body(accountService.getPersonalAccountByEmail(email));
//    }
}
