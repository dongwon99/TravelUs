package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidGroupAccountIdException extends CustomException {

    private static final String DEFAULT_CODE = "GRUOP_ACCOUNT_ID_INVALID";
    private static final int DEFAULT_STATUS = 404;

    public InvalidGroupAccountIdException() {
        super(
            String.format("머니 박스 추가는 그룹 계좌만 가능합니다"),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}