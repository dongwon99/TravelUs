package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.exception.CustomException;

public class DuplicateCurrencyException extends CustomException {

    private static final String DEFAULT_CODE = "DUPLICATE_CURRENCY_CODE";
    private static final int DEFAULT_STATUS = 409;

    public DuplicateCurrencyException(CurrencyType currencyType) {
        super(
            String.format("The CurrencyCode Already Exist: %s", currencyType),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}