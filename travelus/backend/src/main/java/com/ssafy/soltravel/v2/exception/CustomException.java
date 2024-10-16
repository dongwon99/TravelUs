package com.ssafy.soltravel.v2.exception;

import lombok.Data;

@Data
public abstract class CustomException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Internal Server Error";
    private static final String DEFAULT_CODE = "INTERNAL_SERVER_ERROR";
    private static final int DEFAULT_STATUS = 500;

    private String info;
    private String message;
    private String code;
    private int status;

    public CustomException(String message, String code, int status, String info) {
        super(message);
        this.message = message;
        this.code = code;
        this.status = status;
        this.info = info;
    }

    public CustomException() {
        super(DEFAULT_MESSAGE);
        this.message = DEFAULT_MESSAGE;
        this.info = DEFAULT_MESSAGE;
        this.code = DEFAULT_CODE;
        this.status = DEFAULT_STATUS;
    }

    public CustomException(String message, String code, int status) {
        super(message);
        this.message = message;
        this.code = code;
        this.status = status;
    }

}