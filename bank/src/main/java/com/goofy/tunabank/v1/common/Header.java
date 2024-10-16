package com.goofy.tunabank.v1.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class Header {

  private String userKey;
  private String institutionCode;
  private String fineAppNo;
  private String transmissionDate;
  private String transmissionTime;

  public LocalDateTime getTransmissionDateTime() {
    String dateTimeString = transmissionDate + transmissionTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return LocalDateTime.parse(dateTimeString, formatter);
  }
}
