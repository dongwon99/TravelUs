package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.CardHistory;
import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.response.HistoryResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

  @Mapping(target = "transactionUniqueNo", source = "id")
  @Mapping(target = "transactionType", source = "transactionType")
  @Mapping(target = "transactionDate", source = "transactionAt")
  @Mapping(target = "transactionAmount", expression = "java(String.format(\"%.2f\", transactionHistory.getAmount()))")
  @Mapping(target = "transactionBalance", expression = "java(String.format(\"%.2f\", transactionHistory.getBalance()))")
  @Mapping(target = "transactionSummary", source = "summary")
  @Mapping(target = "currencyCode", source = "moneyBox.currency.currencyCode")
  HistoryResponseDto toHistoryResponseDto(TransactionHistory transactionHistory);

  @Mapping(target = "transactionUniqueNo", source = "id")
  @Mapping(target = "transactionType", source = "transactionType")
  @Mapping(target = "payeeName", source = "merchant.name")
  @Mapping(target = "transactionDate", source = "transactionAt")
  @Mapping(target = "transactionAmount", expression = "java(String.format(\"%.2f\", cardHistory.getAmount()))")
  @Mapping(target = "transactionBalance", expression = "java(String.format(\"%.2f\", cardHistory.getBalance()))")
  @Mapping(target = "transactionSummary", expression = "java(String.format(\"%.2f\", cardHistory.getExchangeRate()))")
  @Mapping(target = "currencyCode", source = "currency.currencyCode")
  HistoryResponseDto toHistoryResponseDto(CardHistory cardHistory);

  // 다형성을 처리하기 위한 기본 매핑
  default HistoryResponseDto toHistoryResponseDto(AbstractHistory history) {
    if (history instanceof TransactionHistory) {
      return toHistoryResponseDto((TransactionHistory) history);
    } else if (history instanceof CardHistory) {
      return toHistoryResponseDto((CardHistory) history);
    } else {
      throw new IllegalArgumentException("Unsupported history type");
    }
  }

  // AbstractHistory 리스트를 DTO 리스트로 변환
  List<HistoryResponseDto> toHistoryResponseDtos(List<AbstractHistory> transactionHistories);
}
