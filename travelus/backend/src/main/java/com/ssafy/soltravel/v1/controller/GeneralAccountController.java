package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.account.AccountDto;
import com.ssafy.soltravel.v1.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.v1.dto.account.request.DeleteAccountRequestDto;
import com.ssafy.soltravel.v1.dto.account.response.CreateAccountResponseDto;
import com.ssafy.soltravel.v1.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.v1.service.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GenaralAccount API", description = "일반 계좌 관련 API")
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account/general")
public class GeneralAccountController {

    private final AccountService accountService;

    // ========= 계좌 CRUD =========
    // 계좌 생성 (모임통장의 경우 외화통장도 자동 생성)
    @Operation(
        summary = "계좌 생성", description = "일반 계좌(INDIVIDUAL / GROUP) 선택하여 생성. (accountType / accountPassword는 필수), 나머지 값은 모임계좌일경우만 필수."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "계좌 생성 성공", content = @Content(schema = @Schema(implementation = CreateAccountResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/{userId}")
    public ResponseEntity<CreateAccountResponseDto> createAccount(
        @Parameter(description = "사용자의 userId", example = "1")
        @PathVariable Long userId,
        @RequestBody CreateAccountRequestDto dto
    ) {
        CreateAccountResponseDto generalAccount = accountService.createGeneralAccount(userId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(generalAccount);
    }

    @Operation(
        summary = "사용자의 모든 일반(개인/그룹) 계좌 조회",
        description = "특정 사용자의 모든 일반 계좌를 조회하는 API."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{userId}/all")
    public ResponseEntity<List<AccountDto>> getAllByUserId(
        @Parameter(description = "사용자의 userId", example = "1")
        @PathVariable Long userId
    ) {

        ResponseEntity<List<AccountDto>> responseEntity = accountService.getAllByUserId(userId, false);

        return responseEntity;
    }

    @Operation(summary = "특정 계좌 조회", description = "계좌 번호를 사용하여 일반 계좌 기본정보를 조회하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountDto> getByAccountNo(
        @Parameter(description = "사용자의 일반 계좌 AccountNo(계좌번호)", example = "0889876543210")
        @PathVariable String accountNo) {

        ResponseEntity<AccountDto> responseEntity = accountService.getByAccountNo(accountNo, false);

        return responseEntity;
    }

    // 일반 통장 CRUD
    @Operation(summary = "일반 계좌 삭제", description = "일반 계좌를 삭제하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "계좌 삭제 성공", content = @Content(schema = @Schema(implementation = DeleteAccountResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @DeleteMapping("/{accountNo}")
    public ResponseEntity<DeleteAccountResponseDto> deleteAccount(
        @Parameter(description = "사용자의 일반 계좌 AccountNo(계좌번호). 해지 계좌 잔액 보유 시 환불 계좌 필수", example = "0889876543210")
        @PathVariable String accountNo,
        @RequestBody(required = false) DeleteAccountRequestDto dto
    ) {

        ResponseEntity<DeleteAccountResponseDto> responseEntity = accountService.deleteAccount(accountNo, false, dto);

        return responseEntity;
    }
}
