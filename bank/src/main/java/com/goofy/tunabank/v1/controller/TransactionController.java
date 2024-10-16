package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.common.RecWrapper;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferMBRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.HistoryResponseDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

  private final TransactionService transactionService;

  /**
   * 입금
   */
  @PostMapping("/deposit")
  public ResponseEntity<RecWrapper<TransactionResponseDto>> deposit(
      @RequestBody TransactionRequestDto requestDto) {

    TransactionResponseDto response = transactionService.processTransaction(requestDto, false);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 출금
   */
  @PostMapping("/withdrawal")
  public ResponseEntity<RecWrapper<TransactionResponseDto>> withdrawal(
      @RequestBody TransactionRequestDto requestDto) {

    TransactionResponseDto response = transactionService.processTransaction(requestDto, false);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 이체
   */
  @PostMapping("/transfer/general")
  public ResponseEntity<RecWrapper<List<TransactionResponseDto>>> transfer(
      @RequestBody TransferRequestDto requestDto) {

    List<TransactionResponseDto> response = transactionService.processGeneralTransfer(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 거래 내역 목록 조회
   */
  @PostMapping("/history")
  public ResponseEntity<RecWrapper<Page<HistoryResponseDto>>> getTransactionHistoryList(
      @RequestBody TransactionHistoryListRequestDto requestDto) {

    Page<HistoryResponseDto> response = transactionService.getTransactionHistory(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 거래 내역 단건 조회
   */
  @PostMapping("/history/detail")
  public ResponseEntity<RecWrapper<HistoryResponseDto>> getTransactionHistory(
      @RequestBody TransactionHistoryRequestDto requestDto
  ) {

    HistoryResponseDto response = transactionService.getHistory(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 머니박스 이체
   */
  @PostMapping("/transfer/moneybox")
  public ResponseEntity<RecWrapper<?>> transferBetweenMoneyBoxes(
      @RequestBody TransferMBRequestDto requestDto) {

    List<TransactionResponseDto> response = transactionService.processMoneyBoxTransfer(requestDto, false);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 자동환전
   */
  @PostMapping("/transfer/moneybox/auto")
  public ResponseEntity<RecWrapper<?>> transferBetweenMoneyBoxesAuto(
      @RequestBody TransferMBRequestDto requestDto) {

    List<TransactionResponseDto> response = transactionService.processAutoExchange(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 자동정산입금
   */
  @PostMapping("/settlement/deposit")
  public ResponseEntity<RecWrapper<TransactionResponseDto>> settlementDepositAuto(
      @RequestBody TransactionRequestDto requestDto) {

    TransactionResponseDto response = transactionService.processAutoSettlement(requestDto, true);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 자동정산출금
   */
  @PostMapping("/settlement/withdrawal")
  public ResponseEntity<RecWrapper<TransactionResponseDto>> settlementWithdrawalAuto(
      @RequestBody TransactionRequestDto requestDto) {

    TransactionResponseDto response = transactionService.processAutoSettlement(requestDto, false);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 환전 예상 금액 조회
   */
  @PostMapping("/exchange/estimate")
  public ResponseEntity<RecWrapper<?>> getExchangeEstimate() {

    return null;
  }
}
