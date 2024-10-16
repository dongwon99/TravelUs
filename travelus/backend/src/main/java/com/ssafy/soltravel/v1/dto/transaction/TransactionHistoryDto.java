package com.ssafy.soltravel.v1.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionHistoryDto {

	@Schema(description = "입금 출금 구분 (1,2)", example = "1")
	private String transactionType;

	@Schema(description = "거래 후 잔액", example = "8009.74")
	private String transactionAfterBalance;

	@Schema(description = "거래 고유 번호", example = "3525")
	private String transactionUniqueNo;

	@Schema(description = "거래 계좌 번호", example = "100123456789")
	private String transactionAccountNo;

	@Schema(description = "거래 금액", example = "5000.00")
	private String transactionBalance;

	@Schema(description = "거래 요약내용", example = "입금")
	private String transactionSummary;

	@Schema(description = "거래 일자", example = "20240825")
	private String transactionDate;

	@Schema(description = "거래 시각", example = "125634")
	private String transactionTime;

	@Schema(description = "거래 메모", example = "Lunch Payment")
	private String transactionMemo;

	@Schema(description = "입금 출금 구분명(입금, 출금, 입금(이체), 출금(이체)", example = "출금")
	private String transactionTypeName;
}
