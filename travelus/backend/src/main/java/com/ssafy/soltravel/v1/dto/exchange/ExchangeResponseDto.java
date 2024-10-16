package com.ssafy.soltravel.v1.dto.exchange;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ExchangeResponseDto {

  private ExchangeCurrencyDto exchangeCurrencyDto;
  private AccountInfoDto accountInfoDto;
  private LocalDateTime executed_at;
}
