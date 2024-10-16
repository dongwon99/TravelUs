package com.ssafy.soltravel.v2.exception.group;


import lombok.Data;

@Data
public class InvalidGroupIdException extends RuntimeException {

  private Boolean invalidInvite = false;

  public InvalidGroupIdException() {
    super(String.format("존재하지 않는 그룹 Id입니다."));
  }

  public InvalidGroupIdException(String message) {
    super(message);
    this.invalidInvite = true;
  }
}
