package com.ssafy.soltravel.v2.dto.account.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InquireAccountListRequestDto {

    @Schema(description = "조회할 계좌 타입 ( I / G / A) ", example = "I")
    private String searchType;

}
