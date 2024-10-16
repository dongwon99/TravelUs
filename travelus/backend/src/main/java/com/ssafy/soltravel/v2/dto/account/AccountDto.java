package com.ssafy.soltravel.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    @Schema(description = "계좌의 고유 ID", example = "1")
    private Long accountId;

    @Schema(description = "유저 이름", example = "김싸피")
    private String userName;

    @Schema(description = "계좌 번호", example = "002-4560023-209")
    private String accountNo;

    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "계좌 유형 ( 개인 : I / 모임 : G )", example = "I")
    private AccountType accountType;

    @Schema(description = "은행 코드", example = "1")
    private int bankCode;

    @Schema(description = "계좌에 연결된 머니박스 목록")
    private List<MoneyBoxDto> moneyBoxDtos;

    @Schema(description = "계좌 생성 날짜", example = "2024-01-01T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "계좌 정보가 마지막으로 업데이트된 날짜", example = "2024-02-01T12:34:56")
    private LocalDateTime updatedAt;

}