package com.ssafy.soltravel.v1.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.soltravel.v1.domain.Enum.AccountType;
import com.ssafy.soltravel.v1.dto.currency.CurrencyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    @Schema(description = "계좌 ID", example = "1")
    private Long id;

    @Schema(description = "은행 코드", example = "088")
    private int bankCode;

    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "계좌 번호", example = "0889876543210")
    private String accountNo;

    @Schema(description = "계좌 잔액", example = "1000")
    private Double balance;

    @Schema(description = "계좌 이름", example = "신한은행 일반 모임통장")
    private String accountName;

    @Schema(description = "계좌 유형 (예: 개인 또는 그룹)", example = "GROUP")
    private AccountType accountType;

    @Schema(description = "그룹 이름", example = "SolTravel 모임 통장")
    private String groupName;

    @Schema(description = "아이콘 이름", example = "airPlane")
    private String iconName;

    @Schema(description = "여행 시작 일자", example = "20240830")
    private LocalDate travelStartDate;

    @Schema(description = "여행 종료 일자", example = "20240902")
    private LocalDate travelEndDate;

    @Schema(description = "통화 정보")
    private CurrencyDto currency;

    @Schema(description = "계좌 생성 일자", example = "2024-08-28T10:00:00")
    private String createdAt;

    @Schema(description = "계좌 수정 일자", example = "2024-08-28T12:00:00")
    private String updatedAt;

    @Schema(description = "희망 환율", example = "1390.04")
    private Float preferenceExchange;
}
