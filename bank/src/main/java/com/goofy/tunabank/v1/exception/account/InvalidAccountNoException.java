package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidAccountNoException extends CustomException {
    private static final String DEFAULT_MESSAGE = "The AccountNo Does Not Exist";
    private static final String DEFAULT_CODE = "ACCOUNT_NO_INVALID";
    private static final int DEFAULT_STATUS = 404;

    public InvalidAccountNoException(String accountNo) {
        super(
            DEFAULT_MESSAGE,
            DEFAULT_CODE,
            DEFAULT_STATUS,
            String.format("%s: %s",DEFAULT_MESSAGE, accountNo)
        );
    }
}