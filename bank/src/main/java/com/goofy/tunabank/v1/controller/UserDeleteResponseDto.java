package com.goofy.tunabank.v1.controller;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDeleteResponseDto {
  
  private String status;
  private String message;

  public UserDeleteResponseDto() {
    this.status = "SUCCESS";
    this.message = "탈퇴 처리 완료";
  }
}
