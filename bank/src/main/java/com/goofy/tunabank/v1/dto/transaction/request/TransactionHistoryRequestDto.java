package com.goofy.tunabank.v1.dto.transaction.request;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import lombok.Data;

@Data
public class TransactionHistoryRequestDto {

  long transactionHistoryId;
  TransactionType transactionType;
}
