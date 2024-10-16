package com.goofy.tunabank.v1.dto.account.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goofy.tunabank.v1.dto.account.AccountDto;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값인 필드를 무시
public class CreateAccountResponseDto {

    private AccountDto generalAccount;

    private AccountDto foreignAccount;
}
