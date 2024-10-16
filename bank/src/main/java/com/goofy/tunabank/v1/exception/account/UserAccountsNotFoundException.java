package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class UserAccountsNotFoundException extends CustomException {

    private static final String DEFAULT_CODE = "USER_ACCOUNTS_NOT_FOUND";
    private static final int DEFAULT_STATUS = 404;

    public UserAccountsNotFoundException(Long userId) {
        super(
            String.format("No accounts found for the user with ID : %d", userId),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}