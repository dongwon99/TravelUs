package com.goofy.tunabank.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDto {

    private String status;

    private String message;

    public ResponseDto() {
        this.status = "SUCCESS";
        this.message = "요청 처리 완료";
    }

    public static ResponseDto success() {
        return new ResponseDto();
    }
}
