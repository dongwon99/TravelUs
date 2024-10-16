package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeModeUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.TargetRateUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.targetRate.TargetRateDto;
import com.ssafy.soltravel.v2.service.exchange.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
@Slf4j
@Tag(name = "Exchange API", description = "환전 관련 API")
public class ExchangeController {

  private final ExchangeService exchangeService;

  /**
   * 환율 전체 조회
   */
  @GetMapping("/rate")
  @Operation(summary = "환율 전체 조회", description = "전체 통화의 실시간 환율을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 환율을 조회했습니다.", content = @Content(schema = @Schema(implementation = ExchangeRateResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "404", description = "요청한 통화를 찾을 수 없습니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<List<ExchangeRateResponseDto>> getExchangeRateAll() {

    return ResponseEntity.ok().body(exchangeService.getExchangeRateAll());
  }

  /**
   * 환율 단건 조회
   */
  @GetMapping("/rate/{currencyCode}")
  @Operation(summary = "환율 단건 조회", description = "특정 통화의 실시간 환율을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 환율을 조회했습니다.", content = @Content(schema = @Schema(implementation = ExchangeRateResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "404", description = "요청한 통화를 찾을 수 없습니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<ExchangeRateResponseDto> getExchangeRate(
      @Parameter(description = "조회할 통화의 코드", example = "USD") @PathVariable String currencyCode) {

    return ResponseEntity.ok().body(exchangeService.getExchangeRate(currencyCode));
  }

  /**
   * 희망 환율 저장
   */
  @PostMapping("/rate/target-rate")
  @Operation(summary = "희망 환율 설정", description = "희망 환율을 설정합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 환율을 저장했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<ResponseDto> setTargetRate(
      @RequestBody ExchangeRateRegisterRequestDto requestDto) {
    ResponseDto response=exchangeService.setPreferenceRate(requestDto, false, -1);
    return ResponseEntity.ok().body(response);
  }

  /**
   * 희망 환율 수정
   */
  @PutMapping("/rate/target-rate")
  @Operation(summary = "희망 환율 수정", description = "희망 환율을 수정합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 희망 환율을 수정했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<ResponseDto> updateTargetRate(
      @RequestBody TargetRateUpdateRequestDto requestDto) {
    ResponseDto response=exchangeService.updateTargetRate(requestDto);
    return ResponseEntity.ok().body(response);
  }

  /**
   * 희망 환율 삭제
   */
  @DeleteMapping("/rate/target-rate/{groupId}")
  @Operation(summary = "희망 환율 삭제", description = "희망 환율을 삭제합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 희망 환율을 삭제했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<ResponseDto> deleteTargetRate(
      @PathVariable Long groupId) {
    ResponseDto response=exchangeService.deleteTargetRate(groupId);
    return ResponseEntity.ok().body(response);
  }

  /**
   * 희망 환율 조회
   */
  @GetMapping("/rate/target-rate/{groupId}")
  @Operation(summary = "희망 환율 조회", description = "희망 환율을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 희망 환율을 조회했습니다.", content = @Content(schema = @Schema(implementation = TargetRateDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<TargetRateDto> getTargetRate(
      @PathVariable Long groupId) {
    TargetRateDto response = exchangeService.getTargetRate(groupId);
    return ResponseEntity.ok().body(response);
  }

  /**
   * 환전 모드 변경
   */
  @PutMapping("/mode")
  @Operation(summary = "환전 모드 변경", description = "환전 모드를 수정합니다. exchangeType(NONE:나중에 알아서 할게요, NOW: 지금 바로 환전해주세요, AUTO: 자동환전할게요)", responses = {
      @ApiResponse(responseCode = "200", description = "환전 모드를 수정했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<ResponseDto> updateExchangeMode(
      @RequestBody ExchangeModeUpdateRequestDto requestDto) {
    ResponseDto response = exchangeService.updateExchangeMode(requestDto);
    return ResponseEntity.ok().body(response);
  }

}
