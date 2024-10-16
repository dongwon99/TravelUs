package com.ssafy.soltravel.v2.dto.account.request;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddMoneyBoxRequestDto {

    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "조회할 계좌 번호", example = "002-4262233-209")
    private String accountNo;

    @Schema(description = "통화 코드 ( KRW / USD / JPY / EUR / CNY )", example = "USD")
    private CurrencyType currencyCode;

}
