package com.ssafy.soltravel.v1.dto.account_book;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountHistoryReadResponseDto {

  private String accountNo;
  private Integer transactionCount;
  private List<DayAccountHistory> monthHistoryList;

  public void initList() {
    monthHistoryList = new ArrayList<>();
    for (int i = 0; i < 32; i++) {
      monthHistoryList.add(new DayAccountHistory(0., 0.));
    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DayAccountHistory {

    private Double totalExpenditure;
    private Double totalIncome;

    public void addTotalExpenditure(Double expenditure) {
      totalExpenditure += expenditure;
    }

    public void addTotalIncome(Double income) {
      totalIncome += income;
    }
  }

}
