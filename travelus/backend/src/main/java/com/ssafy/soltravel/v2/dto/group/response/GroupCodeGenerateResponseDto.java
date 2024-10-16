package com.ssafy.soltravel.v2.dto.group.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupCodeGenerateResponseDto {

  @Schema(example = "Skm0l2XtSrSEtrMinYZClw")
  private String groupCode;
}
