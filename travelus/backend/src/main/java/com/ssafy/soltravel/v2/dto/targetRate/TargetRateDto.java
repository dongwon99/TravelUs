package com.ssafy.soltravel.v2.dto.targetRate;

import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TargetRateDto {

  @Schema(description = "희망환율Id",example = "1")
  private Long targetRateId;

  @Schema(description = "설정 금액",example = "150000")
  private Double amount;

  @Schema(description = "희망환율",example = "1500.01")
  private Double rate;

  @Schema(description = "희망환율 완료 상태",example = "NOT_COMPLETED")
  private SettlementStatus status;
}
