package com.ssafy.soltravel.v2.dto.settlement.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalSettlementHistoryDto {

  @Schema(description = "개별 지출 정산 내역 id",example = "1")
  private Long settlementDetailId;

  @Schema(description = "참여자 id",example = "1")
  private Long participantId;

  @Schema(description = "참여자 이름",example = "도경수")
  private String participantName;

  @Schema(description = "참여자 프로필", example="https://my-s3-mingyu.s3.ap-southeast-2.amazonaws.com/profile/%EC%99%95%EA%B4%80%EC%95%B5%EB%AC%B4%EC%83%88.png")
  private String profile;

  @Schema(description = "개별 지출 정산 금액",example = "10000")
  private double amount;

  @Schema(description = "개별 지출 정산 남은 금액",example = "5000")
  private double remainingAmount;

  @Schema(description = "개별 지출 정산 완료 여부(COMPLETED: 정산 완료, NOT_COMPLETED: 정산 미완료)",example = "NOT_COMPLETED")
  private SettlementStatus isSettled;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(description = "개별 지출 정산 요청 시각",example = "2024-10-04'T'14:05:13")
  private LocalDateTime settlementRequestTime;
}
