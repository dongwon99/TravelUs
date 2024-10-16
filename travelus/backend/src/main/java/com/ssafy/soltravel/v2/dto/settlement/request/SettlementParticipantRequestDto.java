package com.ssafy.soltravel.v2.dto.settlement.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SettlementParticipantRequestDto {

  @Schema(description = "참여자Id",example = "1")
  Long participantId;

  @Schema(description = "정산 금액",example = "10000")
  Double amount;
}
