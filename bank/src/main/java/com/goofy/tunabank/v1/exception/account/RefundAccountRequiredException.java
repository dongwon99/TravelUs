package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class RefundAccountRequiredException extends CustomException {

    private static final String DEFAULT_CODE = "REFUND_ACCOUNT_REQUIRED";
    private static final int DEFAULT_STATUS = 400;

    public RefundAccountRequiredException(String accountNo) {
        super(
            String.format("유효하지 않은 환불 계좌입니다. %s", accountNo),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}