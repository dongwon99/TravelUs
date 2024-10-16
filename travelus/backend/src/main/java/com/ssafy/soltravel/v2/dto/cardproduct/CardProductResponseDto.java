package com.ssafy.soltravel.v2.dto.cardproduct;

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
