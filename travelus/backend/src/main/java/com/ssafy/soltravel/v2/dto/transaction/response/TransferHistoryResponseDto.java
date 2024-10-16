package com.ssafy.soltravel.v2.dto.transaction.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransferHistoryResponseDto {

  @Schema(description = "거래 고유 번호", example = "1")
  String transactionUniqueNo;

  @Schema(description = "거래 유형 코드", example = "TD")
  String transactionType;

  @Schema(description = "거래 상대방 계좌 번호", example = "002-92374323-209")
  String accountNo;

  @Schema(description = "거래 상대방 이름", example = "홍길동")
  String ownerName;

  @Schema(description = "거래 날짜", example = "2024-04-01T10:25:00")
  String transactionDate;

  @Schema(description = "거래 금액", example = "10000")
  String transactionAmount;

  @Schema(description = "거래 후 잔액", example = "99990000")
  String transactionBalance;

  @Schema(description = "거래 요약", example = "이체입금 테스트")
  String transactionSummary;
}
