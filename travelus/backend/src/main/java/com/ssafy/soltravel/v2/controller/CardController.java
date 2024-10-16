package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.card.CardIssueRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardPaymentRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardPaymentResponseDto;
import com.ssafy.soltravel.v2.dto.card.CardResponseDto;
import com.ssafy.soltravel.v2.dto.card.CardUsageAmountResponseDto;
import com.ssafy.soltravel.v2.service.card.CardService;
import com.ssafy.soltravel.v2.util.LogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {


  private final CardService cardService;

  @PostMapping("/issue")
  @Operation(summary = "새로운 카드를 발급합니다", description = "카드 발급 요청 정보를 받아 새로운 카드를 발급합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "카드 발급 성공", content = @Content(schema = @Schema(implementation = CardResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema())),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema()))
  })
  public ResponseEntity issueNewCard(@RequestBody CardIssueRequestDto request){
    LogUtil.info("카드 발급 요청", request);
    CardResponseDto response = cardService.createNewCard(request);
    return new ResponseEntity(response, HttpStatus.CREATED);
  }



  @Operation(summary = "카드 목록 조회", description = "사용자의 모든 카드를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"
          , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDto.class)))
  })
  @GetMapping("/list")
  public ResponseEntity getCardList(){
    LogUtil.info("카드 조회 요청");
    List<CardResponseDto> response = cardService.findAllCards();
    return new ResponseEntity(response, HttpStatus.OK);
  }


  @Operation(summary = "카드 결제", description = "주어진 정보로 카드 결제를 처리합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "성공적으로 결제됨",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CardPaymentResponseDto.class))
      ),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema())),
      @ApiResponse(responseCode = "404", description = "카드를 찾을 수 없음", content = @Content(schema = @Schema()))
  })
  @PostMapping("/payment")
  public ResponseEntity makeCardPayment(@RequestBody CardPaymentRequestDto request){
    LogUtil.info("카드 결제", request);
    CardPaymentResponseDto response = cardService.makeCardPayment(request);
    return new ResponseEntity(response, HttpStatus.CREATED);
  }


  @Operation(summary = "카드 사용 금액 조회", description = "카드의 사용 금액을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"
          , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDto.class)))
  })
  @GetMapping("/history/{cardNo}")
  public ResponseEntity getUsageAmount(@PathVariable String cardNo){
    CardUsageAmountResponseDto response = cardService.getUsageAmount(cardNo);
    return new ResponseEntity(response, HttpStatus.OK);
  }
}
