package com.goofy.tunabank.v1.domain.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {

    // I : 개인 통장, G : 그룹 통장, F : 외화 통장
    I("001"),
    G("002"),
    F("003");

    private final String code;

}
