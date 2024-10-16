package com.ssafy.soltravel.v1.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AccountDetailDto {

    @Schema(description = "계좌 ID", example = "1")
    private Long id;

    @Schema(description = "은행 코드", example = "088")
    private String bankCode;

    @Schema(description = "은행 이름", example = "신한은행")
    private String bankName;

    @Schema(description = "계좌 번호", example = "0889683365547051")
    private String accountNo;

    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "통화", example = "KRW")
    private String currency;

    @Schema(description = "계좌 잔액", example = "1000000")
    private Long accountBalance;

    @Schema(description = "계좌 이름", example = "신한은행 모임통장")
    private String accountName;

    @Schema(description = "사용자 이름", example = "qoridhc")
    private String userName;

    @Schema(description = "계좌 유형 코드", example = "1")
    private String accountTypeCode;

    @Schema(description = "계좌 유형 이름", example = "수시입출금")
    private String accountTypeName;

    @Schema(description = "계좌 만료일", example = "20240919")
    private String accountExpiryDate;

    @Schema(description = "일일 이체 한도", example = "50000000")
    private Long dailyTransferLimit;

    @Schema(description = "1회 이체 한도", example = "100000000")
    private Long oneTimeTransferLimit;

    @Schema(description = "계좌 생성일", example = "20240819")
    private String accountCreatedDate;

    @Schema(description = "마지막 거래 날짜", example = "20240919")
    private String lastTransactionDate;

}