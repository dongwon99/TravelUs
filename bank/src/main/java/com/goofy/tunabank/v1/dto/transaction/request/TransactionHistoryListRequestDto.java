package com.goofy.tunabank.v1.dto.transaction.request;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import java.time.LocalDate;
import lombok.Data;

@Data
public class TransactionHistoryListRequestDto {

  private String accountNo;

  private CurrencyType currencyCode;

  private LocalDate startDate;

  private LocalDate endDate;

  private TransactionType transactionType;

  private OrderByType orderByType;

  /**
   * 페이징용 변수
   */
  private Integer page;
  private Integer size;
}
