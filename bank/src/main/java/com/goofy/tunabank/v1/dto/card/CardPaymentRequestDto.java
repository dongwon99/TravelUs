package com.goofy.tunabank.v1.dto.card;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
  private String cardNo;

  @NotEmpty
  @Length(min = 3, max = 3)
  private String cvc;

  @NotNull @Positive
  private Long merchantId;

  @NotNull @Positive
  private Double paymentBalance;

  @NotEmpty
  private String transactionId;

  @NotEmpty
  private String currencyCode;
}
