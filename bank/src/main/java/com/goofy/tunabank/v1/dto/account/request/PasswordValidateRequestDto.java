package com.goofy.tunabank.v1.dto.account.request;

import lombok.Data;

@Data
public class PasswordValidateRequestDto {

  String accountNo;
  String accountPassword;
}
