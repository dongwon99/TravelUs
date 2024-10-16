package com.goofy.tunabank.v1.dto.auth;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyIssueReponseDto {
  private String managerId;
  private String apiKey;
  private LocalDateTime createAt;
  private LocalDateTime expireAt;
}
