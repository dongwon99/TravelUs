package com.goofy.tunabank.v1.dto.cardproduct;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardProductResponseDto {

  private String cardName;
  private String cardDescription;
  private String cardUniqueNo;
}
