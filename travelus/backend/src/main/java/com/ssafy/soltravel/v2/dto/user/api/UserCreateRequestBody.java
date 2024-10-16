package com.ssafy.soltravel.v2.dto.user.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestBody {

  @JsonProperty("Header")
  private Header header;
  private String userId;
  private String userName;

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Header {
    private String apiKey;
  }
}
