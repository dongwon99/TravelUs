package com.ssafy.soltravel.v1.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

  public static LocalDateTime getLocalDateTime(String dateString) {
    LocalDate date = LocalDate.parse(dateString, formatter);
    return date.atStartOfDay();
  }

  public static LocalDateTime getNextLocalDateTime(String dateString) {
    LocalDate date = LocalDate.parse(dateString, formatter);
    return date.plusDays(1).atStartOfDay();
  }

}
