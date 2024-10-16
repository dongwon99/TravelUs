package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequestDto {

    @Schema(description = "계좌번호", example = "001-69042343-209")
    String accountNo;

    @Schema(description = "계좌 비밀번호", example = "1234")
    String accountPassword;

    @Schema(description = "머니박스 통화[KRW,USD]", example = "KRW")
    CurrencyType currencyCode;

    @Schema(description = "거래 타입[D:입금, W:출금]", example = "D")
    TransactionType transactionType;

    @Schema(description = "거래 금액", example = "5000")
    Double transactionBalance;

    @Schema(description = "거래 요약", example = "월급 입금")
    String transactionSummary;
}
