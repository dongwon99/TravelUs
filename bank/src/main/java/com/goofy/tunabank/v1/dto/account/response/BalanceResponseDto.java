package com.goofy.tunabank.v1.dto.account.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BalanceResponseDto {

  double balance;

  public String getBalance() {
    return String.format("%.2f", balance);
  }
}