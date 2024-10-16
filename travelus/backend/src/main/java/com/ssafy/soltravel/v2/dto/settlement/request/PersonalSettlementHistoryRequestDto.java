package com.ssafy.soltravel.v2.dto.settlement.request;

import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PersonalSettlementHistoryRequestDto {

  @Schema(description = "정산 여부(COMPLETED/NOT_COMPLETED)", example = "COMPLETED")
  SettlementStatus settlementStatus;
}
