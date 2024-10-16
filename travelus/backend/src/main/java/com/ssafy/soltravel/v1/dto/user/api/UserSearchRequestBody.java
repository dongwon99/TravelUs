package com.ssafy.soltravel.v1.dto.user.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequestBody {

  private String apiKey;
  private String userId;
}
