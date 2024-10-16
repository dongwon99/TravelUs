package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidBankIdException extends CustomException{
    private static final String DEFAULT_CODE = "BANK_ID_INVALID";
    private static final int DEFAULT_STATUS = 404;

    public InvalidBankIdException(Long bankId) {
        super(
            String.format("BankId Does Not Exist: %s", bankId),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}

