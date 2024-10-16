package com.ssafy.soltravel.v1.dto.account.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.soltravel.v1.dto.account.AccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "계좌 생성 응답 DTO")
@JsonInclude(JsonInclude.Include.NON_NULL) // null인거 제외하고 직렬화
public class CreateAccountResponseDto {

    @Schema(
        description = "일반 계좌 정보",
        implementation = AccountDto.class
    )
    private AccountDto generalAccount;

    @Schema(
        description = "외화 계좌 정보",
        implementation = AccountDto.class
    )
    private AccountDto foreignAccount;

}