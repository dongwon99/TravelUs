package com.ssafy.soltravel.v2.exception.group;

import com.ssafy.soltravel.v2.exception.CustomException;

public class GroupBalanceRemainingException extends CustomException {

    private static final String DEFAULT_MESSAGE = "잔액이 남아있는 모임은 탈퇴가 불가능합니다. 잔액을 먼저 정산해주세요";
    private static final String DEFAULT_CODE = "GROUP_BALANCE_REMAINING";
    private static final int DEFAULT_STATUS = 400;

    public GroupBalanceRemainingException(Long groupId) {
        super(
            DEFAULT_MESSAGE,
            DEFAULT_CODE,
            DEFAULT_STATUS,
            String.format("%s: %d", DEFAULT_MESSAGE, groupId)
        );
    }
}
