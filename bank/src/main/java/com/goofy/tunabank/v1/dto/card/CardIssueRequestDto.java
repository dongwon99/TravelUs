package com.goofy.tunabank.v1.dto.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardIssueRequestDto {

  @JsonProperty("Header")
  private Header header;               // 인증용 공통 헤더

  @NotEmpty
  @Length(min = 8, max = 8)
  private String cardUniqueNo;         // 카드 고유 번호

  @NotEmpty
  @Length(min = 17, max = 17)
  private String withdrawalAccountNo;  // 출금 계좌 번호

  @NotEmpty
  @Length(min = 4, max = 4)
  private String password;
}
