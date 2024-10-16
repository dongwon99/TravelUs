package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.account_book.AccountHistoryReadRequestDto;
import com.ssafy.soltravel.v1.dto.account_book.AccountHistoryReadResponseDto;
import com.ssafy.soltravel.v1.dto.account_book.AccountHistorySaveRequestDto;
import com.ssafy.soltravel.v1.dto.account_book.AccountHistorySaveResponseDto;
import com.ssafy.soltravel.v1.dto.account_book.DetailAccountHistoryReadRequestDto;
import com.ssafy.soltravel.v1.dto.account_book.DetailAccountHistoryReadResponseDto;
import com.ssafy.soltravel.v1.dto.account_book.ReceiptAnalysisDto;
import com.ssafy.soltravel.v1.dto.account_book.ReceiptUploadRequestDto;
import com.ssafy.soltravel.v1.dto.account_book.ReceiptUploadResponseDto;
import com.ssafy.soltravel.v1.exception.LackOfBalanceException;
import com.ssafy.soltravel.v1.service.account_book.AccountBookService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/account-book")
@RequiredArgsConstructor
@Tag(name = "AccountBook", description = "가계부 관련 API")
public class AccountBookController {

  private final AccountBookService accountBookService;

  @Operation(summary = "영수증 업로드", description = "jpg, png, pdf 형식의 영수증을 업로드하고 그 정보를 받아옵니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "업로드 완료", content = @Content(schema = @Schema(implementation = ReceiptUploadResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PostMapping(value = "/upload/receipt", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadReceipt(@ModelAttribute ReceiptUploadRequestDto requestDto) throws IOException {

    LogUtil.info("requested", requestDto.toString());
    ReceiptAnalysisDto response = accountBookService.uploadReceipt(requestDto);
    return ResponseEntity.ok().body(response);
  }


  @Operation(summary = "가계부 등록", description = "영수증 정보(사진 말고 Json 데이터)로 가계부를 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "업로드 완료", content = @Content(schema = @Schema(implementation = ReceiptUploadResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PostMapping("/save/history")
  public ResponseEntity<?> saveAccountHistory(@RequestBody AccountHistorySaveRequestDto requestDto)
      throws LackOfBalanceException {

    LogUtil.info("requested", requestDto.toString());
    AccountHistorySaveResponseDto response = accountBookService.saveAccountHistory(requestDto);
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "가계부 조회", description = "특정 달의 가계부를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "업로드 완료", content = @Content(schema = @Schema(implementation = ReceiptUploadResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @GetMapping("/history/{accountNo}")
  public ResponseEntity<?> getAccountHistory(
      @PathVariable("accountNo") String accountNo,
      @ModelAttribute AccountHistoryReadRequestDto request
  ) {

    LogUtil.info("requested", accountNo, request.toString());
    AccountHistoryReadResponseDto response = accountBookService.findAccountHistory(accountNo, request);
    return ResponseEntity.ok().body(response);
  }


  @Operation(summary = "가계부 상세 조회", description = "특정 날짜의 가계부를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "업로드 완료", content = @Content(schema = @Schema(implementation = ReceiptUploadResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @GetMapping("/history/{accountNo}/detail")
  public ResponseEntity<?> getAccountHistory(
      @PathVariable("accountNo") String accountNo,
      @ModelAttribute DetailAccountHistoryReadRequestDto request
  ) {

    LogUtil.info("requested", accountNo, request.toString());
    List<DetailAccountHistoryReadResponseDto> response = accountBookService.findDetailAccountHistory(accountNo, request);
    return ResponseEntity.ok().body(response);
  }

}
