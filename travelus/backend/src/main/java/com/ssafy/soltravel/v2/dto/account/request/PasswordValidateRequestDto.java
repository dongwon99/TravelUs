package com.ssafy.soltravel.v2.dto.account.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordValidateRequestDto {

  String accountNo;
  String accountPassword;
}
