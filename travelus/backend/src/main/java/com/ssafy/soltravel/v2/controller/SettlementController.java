package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.settlement.request.GroupSettlementHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementTransferRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.response.GroupSettlementResponseDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementHistoryDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.service.settlement.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/settlement")
@Slf4j
@Tag(name = "Settlement API", description = "정산 관련 API")
public class SettlementController {

  private final SettlementService settlementService;

  @Operation(summary = "잔액 정산 수행", description = "정산 요청 데이터를 받아 정산을 수행합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "정산 완료", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = String.class)))})
  @PostMapping
  public ResponseEntity<String> executeSettlement(@RequestBody SettlementRequestDto settlementRequestDto) {
    String response = settlementService.executeSettlement(settlementRequestDto);
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "개별 정산 요청", description = "개별 정산 요청 알림을 신청합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "정산 요청 완료", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = String.class)))})
  @PostMapping("/personal")
  public ResponseEntity<String> registerPersonalSettlement(@RequestBody PersonalSettlementRegisterRequestDto requestDto) {
    String response = settlementService.registerPersonalSettlement(requestDto);
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "개별 정산금 이체", description = "개별 정산금을 모임통장으로 이체합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "정산 완료", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransferHistoryResponseDto.class)))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = String.class)))})
  @PostMapping("/personal/transfer")
  public ResponseEntity<List<TransferHistoryResponseDto>> postPersonalSettlementTransfer(
      @RequestBody PersonalSettlementTransferRequestDto requestDto) {
    ResponseEntity<List<TransferHistoryResponseDto>> response = settlementService.postPersonalSettlementTransfer(requestDto);
    return response;
  }

  @Operation(summary = "개별 정산 요청 내역 개인별 조회", description = "개인의 개별 정산 요청 내역을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "정산 완료", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonalSettlementDto.class)))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = String.class)))})
  @GetMapping("/personal/transfer")
  public ResponseEntity<List<PersonalSettlementDto>> getPersonalSettlementHistory(@ModelAttribute
  PersonalSettlementHistoryRequestDto requestDto) {
    List<PersonalSettlementDto> response = settlementService.getPersonalSettlementHistory(requestDto);
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "개별 정산 요청 내역 모임별 조회", description = "모임의 개별 정산 요청 내역을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "정산 완료", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupSettlementResponseDto.class)))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = String.class)))})
  @GetMapping("/group")
  public ResponseEntity<List<GroupSettlementResponseDto>> getGroupSettlementHistory(@ModelAttribute
  GroupSettlementHistoryRequestDto requestDto) {
    List<GroupSettlementResponseDto> response = settlementService.getGroupSettlementHistory(requestDto);
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "개별 정산 요청 건당 조회", description = "개별 정산 요청 건당 내역을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "정산 완료", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupSettlementResponseDto.class)))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = String.class)))})
  @GetMapping("/personal/transfer/{settlementId}")
  public ResponseEntity<GroupSettlementResponseDto> getSettlementHistory(@PathVariable Long settlementId) {
    GroupSettlementResponseDto response = settlementService.getSettlementHistory(settlementId);
    return ResponseEntity.ok().body(response);
  }
}
