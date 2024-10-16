package com.goofy.tunabank.v1.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeAmountRequestDto {

  private double amount;
  private double exchangeRate;
}
