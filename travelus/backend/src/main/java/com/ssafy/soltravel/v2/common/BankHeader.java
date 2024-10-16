package com.ssafy.soltravel.v2.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BankHeader {

        private String apiKey;
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

        public static BankHeader createHeader(String apiKey, String userKey) {
            LocalDateTime now = LocalDateTime.now();
            String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));

            return BankHeader.builder()
                .apiKey(apiKey)
                .userKey(userKey)
                .institutionCode("00209")
                .fineAppNo("209")
                .transmissionDate(transmissionDate)
                .transmissionTime(transmissionTime)
                .build();
    }
}
