package com.goofy.tunabank.v1.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardUsageAmountResponseDto {

  private Double krwAmount;

  private Double foreignAmount;
}
