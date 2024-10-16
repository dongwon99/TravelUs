package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidAccountIdException extends CustomException {

    private static final String DEFAULT_CODE = "ACCOUNT_ID_INVALID";
    private static final int DEFAULT_STATUS = 404;

    public InvalidAccountIdException(Long accountId) {
        super(
            String.format("The AccountId Does Not Exist: %s", accountId),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}