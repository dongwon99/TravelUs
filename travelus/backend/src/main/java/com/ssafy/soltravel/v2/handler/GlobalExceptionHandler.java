package com.ssafy.soltravel.v2.handler;

import com.ssafy.soltravel.v2.dto.ErrorResponseDto;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.exception.CustomException;
import com.ssafy.soltravel.v2.exception.LackOfBalanceException;
import com.ssafy.soltravel.v2.exception.RefundAccountNotFoundException;
import com.ssafy.soltravel.v2.exception.account.InvalidPersonalAccountException;
import com.ssafy.soltravel.v2.exception.auth.InvalidAuthCodeException;
import com.ssafy.soltravel.v2.exception.auth.InvalidCredentialsException;
import com.ssafy.soltravel.v2.exception.group.InvalidGroupIdException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
    public ResponseEntity<ResponseDto> handleGeneralException(Exception e) {

        ResponseDto errorResponseDto = new ResponseDto();
        errorResponseDto.setStatus("INTERNAL_SERVER_ERROR");
        errorResponseDto.setMessage(e.getMessage());
        e.printStackTrace();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException e) {
        e.printStackTrace();
        return new ResponseEntity<String>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        ResponseDto errorResponse = new ResponseDto(
            "NOT_FOUND",
            String.format("DB에 해당 유저가 없습니다: %d", e.getUserId())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseDto> handleInvalidCredentialsException(
        InvalidCredentialsException e) {
        ResponseDto errorResponse = new ResponseDto(
            "UNAUTHORIZED",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAuthCodeException.class)
    public ResponseEntity<ResponseDto> handleInvalidAuthCodeException(
        InvalidAuthCodeException e) {
        ResponseDto errorResponse = new ResponseDto(
            "BAD REQUEST",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefundAccountNotFoundException.class)
    public ResponseEntity<ResponseDto> handleRefundAccountNotFoundException(RefundAccountNotFoundException e) {
        ResponseDto errorResponse = new ResponseDto(
            "BAD REQUEST",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LackOfBalanceException.class)
    public ResponseEntity<ResponseDto> handleLackOfBalanceException(LackOfBalanceException e) {
        ResponseDto errorResponse = new ResponseDto(
            "BAD REQUEST",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPersonalAccountException.class)
    public ResponseEntity<ResponseDto> handleInvalidGroupAccountException(InvalidPersonalAccountException e) {
        ResponseDto errorResponse = new ResponseDto(
            "BAD REQUEST",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidGroupIdException.class)
    public ResponseEntity<?> handleInvalidGroupIdException(InvalidGroupIdException e) {
        if (e.getInvalidInvite()) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                e.getMessage(),
                "601"
            );
            return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
        }
        ResponseDto errorResponse = new ResponseDto(
            "BAD REQUEST",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ResponseDto errorResponse = new ResponseDto(
            "BAD_REQUESET",
            ""
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
        return buildErrorResponse(e, e.getCode(), HttpStatus.valueOf(e.getStatus()));
    }

}


