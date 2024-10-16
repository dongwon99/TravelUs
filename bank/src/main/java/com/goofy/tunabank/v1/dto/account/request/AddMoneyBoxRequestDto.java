package com.goofy.tunabank.v1.dto.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import lombok.Data;

@Data
public class AddMoneyBoxRequestDto {

    @JsonProperty("Header")
    private Header Header;

    private String accountNo;

    private String accountPassword;

    private CurrencyType currencyCode;

}
