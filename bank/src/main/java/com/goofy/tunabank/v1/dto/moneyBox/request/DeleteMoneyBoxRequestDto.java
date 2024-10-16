package com.goofy.tunabank.v1.dto.moneyBox.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import lombok.Data;

@Data
public class DeleteMoneyBoxRequestDto {

    @JsonProperty("Header")
    private Header header;

    private String accountNo;

    private String accountPassword;

    private CurrencyType currencyCode;

}
