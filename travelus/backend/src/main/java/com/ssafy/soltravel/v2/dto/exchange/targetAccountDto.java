package com.ssafy.soltravel.v2.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class targetAccountDto {

  Long targetId;//목표환율 id
  String accountNo;//모임 계좌번호
  Long userId;//모임주 id
  double amount;
  double targetRate;
  boolean isAll;//전액환전인지
}
