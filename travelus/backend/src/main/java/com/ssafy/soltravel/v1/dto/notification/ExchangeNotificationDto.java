package com.ssafy.soltravel.v1.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeNotificationDto {

  private long accountId;
  private String accountNo;
  private String exchangeRate;
  private String message;
}
