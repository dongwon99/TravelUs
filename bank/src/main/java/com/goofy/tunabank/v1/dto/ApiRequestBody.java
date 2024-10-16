package com.goofy.tunabank.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequestBody {

  private Header header;

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Header {
    @JsonProperty("apiKey")
    private String apikey;

    @JsonProperty("userKey")
    private String userKey;
  }
}
