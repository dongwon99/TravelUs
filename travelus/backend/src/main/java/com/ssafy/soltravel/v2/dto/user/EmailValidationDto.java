package com.ssafy.soltravel.v2.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailValidationDto {

  @Schema(description = "사용자 Id", example = "1")
  private long userId;

  @Schema(description = "사용자 계좌 Id", example = "1")
  private long accountId;

  @Schema(description = "사용자 계좌 번호", example="8888888888")
  private String accountNo;

  @Schema(description = "사용자 이름", example = "박예진")
  private String userName;
}
