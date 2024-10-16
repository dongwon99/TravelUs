package com.ssafy.soltravel.v2.exception.notification;

import com.ssafy.soltravel.v2.exception.CustomException;

public class FcmTokenNotFound extends CustomException {

    private static final String DEFAULT_MESSAGE = "해당 유저의 FCM TOKEN 값이 없습니다.";
    private static final String DEFAULT_CODE = "FCM_TOKEN_NOT_FOUND";
    private static final int DEFAULT_STATUS = 404;

    public FcmTokenNotFound(Long userId) {
        super(
            String.format("해당 유저의 FCM 토큰이 유효하지 않습니다.: %s", userId),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}
