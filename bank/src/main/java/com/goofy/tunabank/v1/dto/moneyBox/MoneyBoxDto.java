package com.goofy.tunabank.v1.dto.moneyBox;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoneyBoxDto {

    private Long moneyBoxId;

    private double balance;

    private CurrencyType currencyCode;

}
