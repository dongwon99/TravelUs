package com.ssafy.soltravel.v2.dto.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.common.BankHeader;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardNoRequestDto {

    private String accountNo;

    @Null
    @JsonProperty("Header")
    @Schema(hidden = true)
    private BankHeader header;

}
