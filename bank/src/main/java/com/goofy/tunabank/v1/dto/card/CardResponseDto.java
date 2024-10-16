package com.goofy.tunabank.v1.dto.card;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDto {

  private String cardNo;
  private String cvc;
  private String cardUniqueNo;
  private String cardIssuerName;
  private String cardName;
  private String cardDescription;
  private LocalDateTime cardExpiryDate;
  private String withdrawalAccountNo;

}
