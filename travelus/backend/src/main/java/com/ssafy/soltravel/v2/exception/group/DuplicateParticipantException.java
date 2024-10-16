package com.ssafy.soltravel.v2.exception.group;

import com.ssafy.soltravel.v2.exception.CustomException;
import lombok.Data;

@Data
public class DuplicateParticipantException extends CustomException {

    private static final String DEFAULT_MESSAGE = "이미 해당 모임에 참여중인 참가자입니다.";
    private static final String DEFAULT_CODE = "DUPLICATE_PARTICIPANT";
    private static final int DEFAULT_STATUS = 400; // Bad Request

    public DuplicateParticipantException(Long groupId, Long userId) {
        super(
            DEFAULT_MESSAGE,
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}
