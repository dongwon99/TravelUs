package com.ssafy.soltravel.v1.dto.account_book;

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
  private List<ItemAnalysisDto> items;
  private double paid;
}
