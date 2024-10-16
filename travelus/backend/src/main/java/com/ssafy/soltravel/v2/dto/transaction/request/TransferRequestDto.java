package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequestDto {

    @Schema(description = "이체 타입[G:일반이체]", example = "G")
    TransferType transferType;

    @Schema(description = "출금 계좌번호", example = "001-69042343-209")
    String withdrawalAccountNo;

    @Schema(description = "입금 계좌번호", example = "001-69042343-209")
    String depositAccountNo;

    @Schema(description = "거래 금액", example = "5000")
    Double transactionBalance;

    @Schema(description = "출금 거래 요약", example = "이체 출금 테스트")
    String withdrawalTransactionSummary;

    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "입금 거래 요약", example = "이체 입금 테스트")
    private String depositTransactionSummary;
}
