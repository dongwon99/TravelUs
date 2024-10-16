package com.ssafy.soltravel.v2.exception.group;

import com.ssafy.soltravel.v2.exception.CustomException;

public class NotGroupMasterException extends CustomException {

    private static final String DEFAULT_MESSAGE = "그룹 삭제는 그룹장만 가능합니다.";
    private static final String DEFAULT_CODE = "NOT_GROUP_MASTER";
    private static final int DEFAULT_STATUS = 403; // Forbidden

    public NotGroupMasterException(Long groupId) {
        super(
            DEFAULT_MESSAGE,
            DEFAULT_CODE,
            DEFAULT_STATUS,
            String.format("%s: 그룹 ID %d", DEFAULT_MESSAGE, groupId)
        );
    }
}
