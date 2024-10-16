package com.ssafy.soltravel.v1.exception;

public class InvalidTokenException extends RuntimeException {

  private final String token;

  // 기본 생성자
  public InvalidTokenException(String token) {
    super("The token provided is invalid: " + token);
    this.token = token;
  }
  
  // 메시지와 토큰을 받는 생성자
  public InvalidTokenException(String message, String token) {
    super(message + ": " + token);
    this.token = token;
  }

  // 메시지, 원인, 토큰을 받는 생성자
  public InvalidTokenException(String message, Throwable cause, String token) {
    super(message + ": " + token, cause);
    this.token = token;
  }

  // 원인과 토큰을 받는 생성자
  public InvalidTokenException(Throwable cause, String token) {
    super("The token provided is invalid: " + token, cause);
    this.token = token;
  }

  // token 값을 반환하는 메서드
  public String getToken() {
    return token;
  }
}
