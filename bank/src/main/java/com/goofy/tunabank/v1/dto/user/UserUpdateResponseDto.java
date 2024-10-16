package com.goofy.tunabank.v1.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class UserUpdateResponseDto {
  private String status;
  private String message;

  public UserUpdateResponseDto() {
    this.status = "SUCCESS";
    this.message = "수정 완료";
  }
}
