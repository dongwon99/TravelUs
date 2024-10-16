package com.goofy.tunabank.v1.dto.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import lombok.Data;

@Data
public class CreateGeneralAccountRequestDto {

    @JsonProperty("Header")
    private Header Header;

    private AccountType accountType;

    private String accountPassword;

    private Long bankId;

}
