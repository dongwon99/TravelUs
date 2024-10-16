package com.goofy.tunabank.v1.dto.account.request;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import lombok.Getter;

@Getter
public class GetAccountRequestDto {
    AccountType accountType;
}
