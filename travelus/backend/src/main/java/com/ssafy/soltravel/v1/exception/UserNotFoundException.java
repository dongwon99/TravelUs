package com.ssafy.soltravel.v1.exception;

import com.ssafy.soltravel.v1.dto.user.UserSearchRequestDto;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

  private final Long userId;

  public UserNotFoundException(UserSearchRequestDto dto) {
    super(String.format("User Not Found: %s", dto.toString()));
    this.userId = dto.getUserId();
  }

  public UserNotFoundException(Long userId) {
    super(String.format("User Not Found: %d", userId));
    this.userId = userId;
  }

  public UserNotFoundException(String email) {
    super(String.format("User Not Found: %s", email));
    userId = 0L;
  }
}
