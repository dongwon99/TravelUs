package com.goofy.tunabank.v1.dto.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import lombok.Data;

@Data
public class InquireAccountRequestDto {

    @JsonProperty("Header")
    private Header Header;

    private String accountNo;

}
