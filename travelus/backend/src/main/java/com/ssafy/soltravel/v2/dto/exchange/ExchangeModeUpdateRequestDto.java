package com.ssafy.soltravel.v2.dto.exchange;

import com.ssafy.soltravel.v2.domain.Enum.ExchangeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExchangeModeUpdateRequestDto {

  @Schema(description = "그룹Id", example = "1")
  long groupId;

  @Schema(description = "환전 타입", example = "NOW")
  ExchangeType exchangeType;
}
