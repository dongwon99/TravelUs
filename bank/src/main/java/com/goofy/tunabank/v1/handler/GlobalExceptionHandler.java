package com.goofy.tunabank.v1.handler;

import com.goofy.tunabank.v1.dto.ErrorResponseDto;
import com.goofy.tunabank.v1.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 공통 응답 생성 메서드
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(Exception e, String message, HttpStatus status) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(status.getReasonPhrase());
        errorResponseDto.setMessage(message);
        errorResponseDto.setErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorResponseDto, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception e) {
        return buildErrorResponse(e, "내부 서버 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(Exception e) {
        return buildErrorResponse(e, "잘못된 파라미터 요청입니다. 요청 파라미터를 다시 확인해주세요", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
        return buildErrorResponse(e, e.getCode(), HttpStatus.valueOf(e.getStatus()));
    }
}
