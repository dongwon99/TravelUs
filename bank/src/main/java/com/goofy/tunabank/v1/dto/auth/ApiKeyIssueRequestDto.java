package com.goofy.tunabank.v1.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyIssueRequestDto {

  @NotEmpty(message = "managerId는 필수 값입니다.")
  @Email(message = "유효한 이메일 형식이어야 합니다.")
  @JsonProperty("managerId")
  private String managerId;
}
