package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidAccountPasswordException extends CustomException {
    private static final String DEFAULT_CODE = "ACCOUNT_PASSWORD_INVALID";
    private static final int DEFAULT_STATUS = 404;

    public InvalidAccountPasswordException(String accountPassword) {
        super(
            String.format("AccountPassword Does Not Match: %s", accountPassword),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}