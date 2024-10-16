package com.ssafy.soltravel.v1.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionNotificationDto {

  private long userId;
  private long accountId;
  private String accountNo;
  private double balance;
  private double amount;
  private String message;
}
