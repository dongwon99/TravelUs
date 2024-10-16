package com.ssafy.soltravel.v2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    @Schema(description = "응답 상태", example = "FAIL")
    private String status;

    @Schema(description = "응답 메시지", example = "요청 처리 완료.")
    private String message;

    @Schema(example = "601")
    private String code;

    @Schema(example = "601")
    private String errorMessage;


    public ErrorResponseDto() {
        this.status = "SUCCESS";
        this.message = "요청 처리 완료";
    }

    public ErrorResponseDto(String message, String code) {
        this.status = "FAIL";
        this.message = message;
        this.code = code;
    }

    public ErrorResponseDto(String message, String code, HttpStatus status) {
        this.status = status.toString();
        this.message = message;
        this.code = code;
    }

    public static ResponseEntity<ErrorResponseDto> databaseError(String message) {
        ErrorResponseDto responseBody = new ErrorResponseDto("Database Error", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ErrorResponseDto> validationFail(String message) {
        ErrorResponseDto responseBody = new ErrorResponseDto("Validation Failed", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
