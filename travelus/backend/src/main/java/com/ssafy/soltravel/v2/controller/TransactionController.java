package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionHistoryListRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.HistoryResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransactionResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Transaction API", description = "거래 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

  private final TransactionService transactionService;

  @Operation(summary = "계좌 입금", description = "지정된 계좌에 입금합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "입금 성공", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/deposit")
  public ResponseEntity<TransactionResponseDto> postAccountDeposit(
      @RequestBody TransactionRequestDto requestDto) {

    ResponseEntity<TransactionResponseDto> response = transactionService.postAccountDeposit(
        requestDto, -1);
    return response;
  }

  @Operation(summary = "계좌 출금", description = "지정된 계좌에서 출금합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "출금 성공", content = @Content(schema = @Schema(implementation = TransactionResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/withdrawal")
  public ResponseEntity<TransactionResponseDto> postAccountWithdrawal(
      @RequestBody TransactionRequestDto requestDto) {

    ResponseEntity<TransactionResponseDto> response = transactionService.postAccountWithdrawal(
        requestDto, -1);
    return response;
  }

  @Operation(summary = "일반 이체", description = "원화 계좌에서 다른 원화 계좌로 이체합니다.")
  @ApiResponses(value = {

      @ApiResponse(responseCode = "200", description = "이체 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransferHistoryResponseDto.class)))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/transfer/general")
  public ResponseEntity<List<TransferHistoryResponseDto>> postAccountTransfer(
      @RequestBody TransferRequestDto requestDto) {

    ResponseEntity<List<TransferHistoryResponseDto>> response = transactionService.postGeneralTransfer(
        requestDto);
    return response;
  }

  @Operation(summary = "머니 박스 이체", description = "환전 및 재환전을 합니다.")
  @ApiResponses(value = {

      @ApiResponse(responseCode = "200", description = "이체 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransferHistoryResponseDto.class)))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/transfer/moneybox")
  public ResponseEntity<List<TransferHistoryResponseDto>> postMoneyBoxTransfer(
      @RequestBody MoneyBoxTransferRequestDto requestDto) {

    ResponseEntity<List<TransferHistoryResponseDto>> response = transactionService.postMoneyBoxTransfer(
        requestDto, false, -1);
    return response;
  }


  // 거래 내역 목록 조회
  @Operation(summary = "거래 내역 목록 조회", description = "지정된 계좌의 거래 내역을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "거래 내역 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoryResponseDto.class)))),
      @ApiResponse(responseCode = "404", description = "거래 내역 없음", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @GetMapping("/history")
  public ResponseEntity<Page<HistoryResponseDto>> getHistoryListByAccountNo(
      @ModelAttribute TransactionHistoryListRequestDto requestDto) {

    ResponseEntity<Page<HistoryResponseDto>> response = transactionService.getHistoryList(
        requestDto);
    return response;
  }

  // 거래 내역 단건 조회
  @Operation(summary = "거래 내역 단건 조회", description = "지정된 계좌의 거래 내역을 단건 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "거래 내역 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoryResponseDto.class)))),
      @ApiResponse(responseCode = "404", description = "거래 내역 없음", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @GetMapping("/history/detail")
  public ResponseEntity<HistoryResponseDto> getHistoryByAccountNo(
      @ModelAttribute TransactionHistoryRequestDto requestDto) {

    ResponseEntity<HistoryResponseDto> response = transactionService.getHistory(requestDto);
    return response;
  }
}
