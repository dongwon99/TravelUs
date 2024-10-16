package com.ssafy.soltravel.v2.dto.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.common.BankHeader;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentRequestDto {

  @NotEmpty
  @Length(min = 16, max = 16)
  @Schema(description = "카드 번호", example = "1234567812345678", required = true)
  private String cardNo;

  @NotEmpty
  @Length(min = 3, max = 3)
  @Schema(description = "카드의 CVC 번호", example = "123", required = true)
  private String cvc;

  @NotNull
  @Positive
  @Schema(description = "결제할 가맹점의 ID", example = "1", required = true)
  private Long merchantId;

  @NotNull
  @Positive
  @Schema(description = "결제 금액", example = "20.00", required = true)
  private Double paymentBalance;

  @Null
  @Hidden
  private String transactionId;

  @NotEmpty
  @Schema(description = "통화 코드 (예: USD, KRW 등)", example = "USD", required = true)
  private String currencyCode;

  @Schema(hidden = true)
  @Null
  @JsonProperty("Header")
  BankHeader header;
}
