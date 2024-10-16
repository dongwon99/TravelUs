package com.ssafy.soltravel.v2.dto.card;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(name = "카드 번호", description = "16자리 카드번호입니다.")
  private String cardNo;

  @Schema(name = "보안코드", description = "3자리 수의 보안코드입니다.")
  private String cvc;

  @Schema(name = "카드 상품 번호", description = "카드 상품 식별 번호입니다.")
  private String cardUniqueNo;
  
  @Schema(name = "카드사", description = "카드사 이름입니다.(튜나뱅크)")
  private String cardIssuerName;

  @Schema(name = "카드 상품 이름")
  private String cardName;

  @Schema(name = "카드 설명")
  private String cardDescription;

  @Schema(name = "카드 유효기간", description = "5년")
  private String cardExpiryDate;

  @Schema(name = "연결 계좌", description = "입력값과 동일")
  private String withdrawalAccountNo;

}
