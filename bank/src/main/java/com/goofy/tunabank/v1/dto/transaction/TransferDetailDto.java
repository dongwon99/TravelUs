package com.goofy.tunabank.v1.dto.transaction;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.Enum.TransferType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferDetailDto {

  private TransferType transferType;
  private MoneyBox withdrawalBox;
  private MoneyBox depositBox;
  private double withdrawalAmount;
  private double depositAmount;
  private String withdrawalSummary;
  private String depositSummary;
  private LocalDateTime transmissionDateTime;
}
