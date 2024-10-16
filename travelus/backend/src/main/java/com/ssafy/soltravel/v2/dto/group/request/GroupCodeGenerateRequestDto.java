package com.ssafy.soltravel.v2.dto.group.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupCodeGenerateRequestDto {

  @Schema(name = "groupId", description = "그룹 ID를 코드로 바꿔드립니다.", example = "3")
  private Long groupId;
}
