package com.ssafy.soltravel.v2.exception.group;

import com.ssafy.soltravel.v2.exception.CustomException;

public class GroupNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Group not found for group ID.";
  private static final String DEFAULT_CODE = "GROUP_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public GroupNotFoundException(Long groupId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %d", DEFAULT_MESSAGE, groupId)
    );
  }
}
