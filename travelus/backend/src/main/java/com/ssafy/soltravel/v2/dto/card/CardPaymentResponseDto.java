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
public class CardPaymentResponseDto {

  @Schema(name = "가맹점 ID", description = "결제한 가맹점의 ID 값")
  private String merchantId;

  @Schema(name = "가맹점 이름", description = "결제한 가맹점의 이름")
  private String merchantName;

  @Schema(name = "카드 번호", description = "16자리 카드번호입니다.")
  private String category;
  private String paymentAt;

  private String currencyCode;
  private String paymentBalance;

}