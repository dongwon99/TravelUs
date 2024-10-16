package com.goofy.tunabank.v1.exception.auth;

import com.goofy.tunabank.v1.exception.CustomException;
import lombok.Data;

@Data
public abstract class AuthException extends CustomException {
  private String apiKey;

  public AuthException(String message, String apiKey, String code, int status, String info) {
    super(message, code, status, info);
    this.apiKey = apiKey;
  }
}

