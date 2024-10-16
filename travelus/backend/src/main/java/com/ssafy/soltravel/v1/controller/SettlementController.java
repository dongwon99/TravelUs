package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.settlement.SettlementRequestDto;
import com.ssafy.soltravel.v1.service.settlement.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/settlement")
@Slf4j
@Tag(name = "Settlement API", description = "정산 관련 API")
public class SettlementController {

  private final SettlementService settlementService;

  @Operation(summary = "정산 수행", description = "정산 요청 데이터를 받아 정산을 수행합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "정산 완료",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = String.class)))
  })
  @PostMapping
  public ResponseEntity<?> executeSettlement(
      @RequestBody SettlementRequestDto settlementRequestDto) {

    return settlementService.executeSettlement(settlementRequestDto);
  }
}
