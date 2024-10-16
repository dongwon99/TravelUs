package com.ssafy.soltravel.v2.dto.settlement.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class PersonalSettlementRegisterRequestDto {

  @Schema(description = "그룹 id")
  private Long groupId;

  @Schema(description = "정산 총액")
  private Double totalAmount;

  @Schema(description = "참여자별 정산금액")
  private List<SettlementParticipantRequestDto> participants;
}
