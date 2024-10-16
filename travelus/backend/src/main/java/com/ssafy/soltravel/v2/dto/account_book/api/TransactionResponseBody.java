package com.ssafy.soltravel.v2.dto.account_book.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponseBody {

  private String totalElements;
  private List<TransactionContent> content;


  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TransactionContent {

    private String transactionUniqueNo;
    private String transactionType;
    private String payeeName;
    private LocalDateTime transactionDate;
    private Double transactionAmount;
    private Double transactionBalance;
    private String transactionSummary;
    private CurrencyType currencyCode;
  }
}