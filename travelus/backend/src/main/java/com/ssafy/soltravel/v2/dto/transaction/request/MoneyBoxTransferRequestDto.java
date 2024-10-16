package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoneyBoxTransferRequestDto {

    @Schema(description = "이체 타입[M:머니박스이체(환전/재환전)]", example = "M")
    TransferType transferType;

    @Schema(description = "출금 계좌 번호", example = "002-45579486-209")
    String accountNo;
    @Schema(description = "환전 신청 moneyBox의 통화Code", example = "KRW")
    CurrencyType sourceCurrencyCode;
    @Schema(description = "환전 금액이 입금될 moneyBox의 통화Code", example = "USD")
    CurrencyType targetCurrencyCode;
    @Schema(description = "환전 신청 금액", example = "250000")
    Double transactionBalance;
    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    public static MoneyBoxTransferRequestDto create(
        TransferType transferType,
        String accountNo,
        String accountPassword,
        CurrencyType sourceCurrencyCode,
        CurrencyType targetCurrencyCode,
        Double transactionBalance) {

        MoneyBoxTransferRequestDto requestDto = new MoneyBoxTransferRequestDto();
        requestDto.setTransferType(transferType);
        requestDto.setAccountNo(accountNo);
        requestDto.setAccountPassword(accountPassword);
        requestDto.setSourceCurrencyCode(sourceCurrencyCode);
        requestDto.setTargetCurrencyCode(targetCurrencyCode);
        requestDto.setTransactionBalance(transactionBalance);

        return requestDto;
    }
}
