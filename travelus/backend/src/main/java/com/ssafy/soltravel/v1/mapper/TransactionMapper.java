package com.ssafy.soltravel.v1.mapper;

import com.ssafy.soltravel.v1.domain.Enum.OrderByType;
import com.ssafy.soltravel.v1.dto.account_book.AccountHistoryReadRequestDto;
import com.ssafy.soltravel.v1.dto.account_book.DetailAccountHistoryReadRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.request.TransactionHistoryRequestDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionMapper {

  public static TransactionHistoryRequestDto convertAccountToTransaction(
      AccountHistoryReadRequestDto accountDto
  ) {
    return TransactionHistoryRequestDto.builder()
        .startDate(accountDto.getStartDate())
        .endDate(accountDto.getEndDate())
        .transactionType(accountDto.getTransactionType())
        .orderByType(accountDto.getOrderByType())
        .build();
  }


  public static TransactionHistoryRequestDto convertDetailAccountToTransaction(
      DetailAccountHistoryReadRequestDto detailDto
  ) {
    // "yyyymmdd" 형식의 문자열을 LocalDate로 변환
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate date = LocalDate.parse(detailDto.getDate(), formatter);

    // 다음 날로 변경
    LocalDate nextDay = date.plusDays(1);

    // 다시 "yyyymmdd" 형식의 문자열로 변환
    String endDate = nextDay.format(formatter);

    return TransactionHistoryRequestDto.builder()
        .startDate(detailDto.getDate())
        .endDate(endDate)
        .transactionType(detailDto.getTransactionType())
        .orderByType(OrderByType.ASC)
        .build();
  }

}
