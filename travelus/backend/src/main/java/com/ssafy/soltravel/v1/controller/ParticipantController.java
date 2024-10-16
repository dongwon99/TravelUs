package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.ResponseDto;
import com.ssafy.soltravel.v1.dto.account.AccountDto;
import com.ssafy.soltravel.v1.dto.participants.request.AddParticipantRequestDto;
import com.ssafy.soltravel.v1.dto.participants.request.ParticipantListResponseDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Participant API", description = "모임 통장 참여자 관련 API")
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account")
public class ParticipantController {

    private final AccountService accountService;

    // ========= 참가자 CRUD =========
    @Operation(summary = "참가자 추가", description = "특정 계좌에 새로운 참가자를 추가하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "참가자 추가 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/{accountId}/participants")
    public ResponseEntity<ResponseDto> postParticipant(
        @Parameter(description = "사용자의 계좌ID accountId(계좌 ID)", example = "1")
        @PathVariable Long accountId,
        @RequestBody AddParticipantRequestDto requestDto
    ) {

        ResponseEntity<ResponseDto> response = accountService.addParticipant(accountId, requestDto);

        return response;
    }

    @Operation(summary = "특정 모임의 모든 참가자 조회", description = "특정 계좌의 모든 참여자를 조회하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ParticipantListResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "참가자를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{accountId}/participants")
    public ResponseEntity<ParticipantListResponseDto> getParticipant(
        @Parameter(description = "사용자의 계좌ID accountId(계좌 ID)", example = "1")
        @PathVariable Long accountId
    ) {

        ResponseEntity<ParticipantListResponseDto> response = accountService.getParticipants(accountId);

        return response;
    }

    @Operation(summary = "특정 유저가 가입한 (생성 X)모든 모임 통장 정보 조회", description = "특정 유저가 가입한 모든 모임 통장 정보 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class)))),
        @ApiResponse(responseCode = "404", description = "참가자를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{userId}/participants/all")
    public ResponseEntity<List<AccountDto>> getGroupInfoByUserId(
        @Parameter(description = "조회 하려는 사용자의 ID(userId)", example = "1")
        @PathVariable Long userId
    ) {

        ResponseEntity<List<AccountDto>> response = accountService.getAllGroupInfoByUserId(userId);

        return response;
    }

    @Operation(summary = "참가자 모임통장 탈퇴", description = "모임통장에서 참여자를 탈퇴시키는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "참가자를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @DeleteMapping("/{participantId}/participants")
    public ResponseEntity<ResponseDto> deleteParticipant(
        @Parameter(description = "탈퇴하려는 사용자의 참여 ID(participantId)", example = "1")
        @PathVariable Long participantId
    ) {

        ResponseEntity<ResponseDto> response = accountService.deleteParticipants(participantId);

        return response;
    }
}
