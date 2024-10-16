package com.ssafy.soltravel.v2.dto.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequestDto {

  @JsonProperty("profileImg")
  @NotNull
  @Parameter(description = "사용자의 프로필 이미지 파일", schema = @Schema(type = "string", format = "binary"))
  MultipartFile profileImg;
}
