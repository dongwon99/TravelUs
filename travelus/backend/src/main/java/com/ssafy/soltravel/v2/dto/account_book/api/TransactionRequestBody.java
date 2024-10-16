package com.ssafy.soltravel.v2.dto.account_book.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.common.BankHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestBody {

  @JsonProperty("Header")
  private BankHeader header;
  private String accountNo;
  private String startDate;
  private String endDate;
  private String currencyCode;

}