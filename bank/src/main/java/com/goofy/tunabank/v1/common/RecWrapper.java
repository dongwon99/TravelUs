package com.goofy.tunabank.v1.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecWrapper<T> {

  @JsonProperty("REC")
  private T REC;
}
