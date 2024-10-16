package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionHistoryRequestDto {

  @Schema(description = "거래 고유 번호", example = "1")
  String transactionHistoryId;

  @Schema(description = "거래 유형 코드", example = "TD")
  TransactionType transactionType;
}
