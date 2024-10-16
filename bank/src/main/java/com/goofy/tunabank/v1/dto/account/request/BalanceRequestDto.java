package com.goofy.tunabank.v1.dto.account.request;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import lombok.Data;

@Data
public class BalanceRequestDto {

  private String accountNo;
  private CurrencyType currencyCode;
}