package com.ssafy.soltravel.v1.dto.transaction.request;

import com.ssafy.soltravel.v1.domain.Enum.OrderByType;
import com.ssafy.soltravel.v1.domain.Enum.TransacntionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryRequestDto {

    @Schema(description = "조회할 거래의 시작 날짜", example = "20240822")
    private String startDate;

    @Schema(description = "조회할 거래의 종료 날짜", example = "20241010")
    private String endDate;

    @Schema(description = "거래 유형 (M:입금, D:출금, A:전체)", example = "A")
    private TransacntionType transactionType;

    @Schema(description = "정렬 기준 (ASC: 오름차순(이전거래), DESC: 내림차순(최근거래))", example = "ASC")
    private OrderByType orderByType;
}
