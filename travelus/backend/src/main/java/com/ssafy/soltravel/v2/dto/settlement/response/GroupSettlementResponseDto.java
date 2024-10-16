package com.ssafy.soltravel.v2.dto.settlement.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupSettlementResponseDto {

  @Schema(description = "개별 지출 정산 내역 id", example = "1")
  private Long personalSettlementId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(description = "개별 지출 정산 요청 시각", example = "2024-10-04'T'14:05:13")
  private LocalDateTime settlementRequestTime;

  @Schema(description = "정산 총 금액", example = "10000")
  private double totalAmount;

  @Schema(description = "정산 남은 금액", example = "10000")
  private double remainingAmount;

  @Schema(description = "개별 지출 정산 완료 여부(COMPLETED: 정산 완료, NOT_COMPLETED: 정산 미완료)", example = "NOT_COMPLETED")
  private SettlementStatus isSettled;

  @Schema(description = "정산 대상 참여자 목록")
  private List<PersonalSettlementHistoryDto> participants;
}
