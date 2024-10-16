package com.goofy.tunabank.v1.dto.transaction.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDto {

  //거래 기록 id
  private Long transactionUniqueNo;

  //거래 종류
  private TransactionType transactionType;

  //상대방 계좌
  private String accountNo;

  //상대방 이름
  private String ownerName;

  //거래 일시
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime transactionDate;

  //금액
  private String transactionAmount;

  //잔액
  private String transactionBalance;

  //메모(환율도)
  private String transactionSummary;

  //거래 통화 코드
  private CurrencyType currencyCode;
}
