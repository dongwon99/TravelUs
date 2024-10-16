package com.ssafy.soltravel.v2.exception.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.soltravel.v2.exception.CustomException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationNotFoundException extends CustomException {

    private static final String DEFAULT_MESSAGE = "알림 아이디에 해당하는 알림이 존재하지 않습니다.";
    private static final String DEFAULT_CODE = "NOTIFICATION_NOT_FOUND";
    private static final int DEFAULT_STATUS = 404;

    public NotificationNotFoundException(Long notificationId) {
        super(
            DEFAULT_CODE,
            DEFAULT_MESSAGE,
            DEFAULT_STATUS,
            null
        );
    }
}
