package com.goofy.tunabank.v1.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Enum.TransferType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferRequestDto {

    //헤더
    @JsonProperty("Header")
    private Header header;

    //이체 종류
    private TransferType transferType;

    //출금 계좌번호
    private String withdrawalAccountNo;

    //계좌 비밀번호
    private String accountPassword;

    //입금 계좌번호
    private String depositAccountNo;

    //금액
    private double transactionBalance;

    //출금 메모
    private String withdrawalTransactionSummary;

    //입금 메모
    private String depositTransactionSummary;

}
