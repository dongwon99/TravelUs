package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.account.AccountDto;
import com.ssafy.soltravel.v1.dto.account.request.DeleteAccountRequestDto;
import com.ssafy.soltravel.v1.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.v1.service.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ForeignAccount API", description = "외화 계좌 관련 API")
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account/foreign")
public class ForeignAccountController {

    private final AccountService accountService;

    // ========= 계좌 CRUD =========
    // 외화통장 CRUD

    @Operation(summary = "사용자의 모든 외환 계좌 조회", description = "특정 사용자의 모든 외환 계좌를 조회하는 API.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountDto.class)))
        ),
        @ApiResponse(responseCode = "404", description = "사용자 또는 계좌를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{userId}/all")
    public ResponseEntity<List<AccountDto>> getAllForeignByUserId(
        @Parameter(description = "사용자의 userId", example = "1")
        @PathVariable Long userId
    ) {

        ResponseEntity<List<AccountDto>> responseEntity = accountService.getAllByUserId(userId, true);

        return responseEntity;
    }

    @Operation(summary = "외화 계좌 조회 - (AccountNo)", description = "외화 계좌 번호를 사용하여 외환 계좌를 조회하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountDto> getForeignByAccountNo(
        @Parameter(description = "사용자의 외화 계좌 AccountNo(계좌번호)", example = "0889876543210")
        @PathVariable String accountNo
    ) {

        ResponseEntity<AccountDto> responseEntity = accountService.getByAccountNo(accountNo, true);

        return responseEntity;
    }

    @Operation(summary = "외화 계좌 조회 - (AccountId)", description = "외화 계좌 Id를 사용하여 외환 계좌를 조회하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AccountDto.class))),
        @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/accountId/{accountId}")
    public ResponseEntity<AccountDto> getForeignByAccountId(
        @Parameter(description = "사용자의 외화 계좌 AccountId(계좌Id)", example = "1")
        @PathVariable String accountId
    ) {

        ResponseEntity<AccountDto> responseEntity = accountService.getForeignByAccountId(Long.valueOf(accountId));

        return responseEntity;
    }


    @Operation(summary = "외화 계좌 삭제", description = "외환 계좌를 삭제하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "계좌 삭제 성공", content = @Content(schema = @Schema(implementation = DeleteAccountResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @DeleteMapping("/{accountNo}")
    public ResponseEntity<DeleteAccountResponseDto> deleteForeignAccount(
        @Parameter(description = "사용자의 외화 계좌 AccountNo(계좌번호)", example = "0889876543210")
        @PathVariable String accountNo,
        @RequestBody(required = false) DeleteAccountRequestDto dto
    ) {

        ResponseEntity<DeleteAccountResponseDto> responseEntity = accountService.deleteAccount(accountNo, true, dto);

        return responseEntity;
    }

}
