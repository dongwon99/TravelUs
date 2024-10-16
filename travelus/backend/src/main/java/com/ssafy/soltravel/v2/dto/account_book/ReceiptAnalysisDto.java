package com.ssafy.soltravel.v2.dto.account_book;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptAnalysisDto {

  private String store;
  private double paid;
  private String transactionAt;
  private String address;
  private String currency;
  private List<ItemAnalysisDto> items;
}
