package com.ssafy.soltravel.v2.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthOcrIdCardRequestDto {

  @Schema(description = "신분증 사진")
  MultipartFile file;

}
