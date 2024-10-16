package com.ssafy.soltravel.v2.dto.transaction.response;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HistoryResponseDto {

  //거래 기록 id
  @Schema(description = "거래 고유 번호", example = "1")
  private Long transactionUniqueNo;

  //거래 종류
  @Schema(description = "거래 종류", example = "TD")
  private String transactionType;

  //사용처(상대방이름)
  @Schema(description = "사용처(상대방이름)", example = "홍길동/이마트 동구미점")
  private String payeeName;

  //거래 일시
  @Schema(description = "거래 일시", example = "2024-04-01T10:25:00")
  private String transactionDate;

  //금액
  @Schema(description = "거래 금액", example = "5000")
  private String transactionAmount;

  //잔액
  @Schema(description = "거래 후 계좌 잔액", example = "5000")
  private String transactionBalance;

  //메모
  @Schema(description = "일반거래시 -> 거래 메모, 카드결제 -> 환율", example = "계좌 입금/1323.01")
  private String transactionSummary;

  //통화 코드
  @Schema(description = "거래 통화 코드", example = "KRW")
  private CurrencyType currencyCode;
}
