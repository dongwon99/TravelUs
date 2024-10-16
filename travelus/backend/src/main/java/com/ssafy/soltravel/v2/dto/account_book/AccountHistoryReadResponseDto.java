package com.ssafy.soltravel.v2.dto.account_book;

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
  private List<DayAccountHistory> monthHistoryList;

  public static AccountHistoryReadResponseDto create(String accountNo) {
    AccountHistoryReadResponseDto dto = new AccountHistoryReadResponseDto();
    dto.accountNo = accountNo;
    dto.monthHistoryList = new ArrayList<>();
    for (int i = 0; i < 32; i++) {
      dto.monthHistoryList.add(new DayAccountHistory());
    }
    return dto;
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class DayAccountHistory {

    private Double totalExpenditureKRW;
    private Double totalExpenditureForeign;
    private Double totalIncomeKRW;
    private Double totalIncomeForeign;

    public DayAccountHistory(){
      totalExpenditureKRW = 0.;
      totalExpenditureForeign = 0.;
      totalIncomeKRW = 0.;
      totalIncomeForeign = 0.;
    }


    public void addTotalExpenditureKRW(Double expenditureKRW) {
      this.totalExpenditureKRW += expenditureKRW;
    }

    public void addTotalExpenditureForeign(Double expenditureForeign) {
      this.totalExpenditureForeign += expenditureForeign;
    }

    public void addTotalIncomeKRW(Double totalIncomeKRW) {
      this.totalIncomeKRW += totalIncomeKRW;
    }

    public void addTotalIncomeForeign(Double totalIncomeForeign) {
      this.totalIncomeForeign += totalIncomeForeign;
    }
  }

}
