package com.goofy.tunabank.v1.exception.moneybox;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidDeleteCurrencyException extends CustomException {

    private static final String DEFAULT_MESSAGE = "원화 머니박스는 해지가 불가능합니다. 계좌해지를 시도해주세요";
    private static final String DEFAULT_CODE = "CURRENCY_DELETION_INVALID";
    private static final int DEFAULT_STATUS = 400; // HTTP 400 Bad Request

    public InvalidDeleteCurrencyException() {
        super(
            DEFAULT_MESSAGE,
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}
