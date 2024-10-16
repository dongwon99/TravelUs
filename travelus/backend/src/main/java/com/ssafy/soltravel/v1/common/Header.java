package com.ssafy.soltravel.v1.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import lombok.Data;

@Data
public class Header {

    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    private String apiKey;
    private String userKey;

    // Private constructor to enforce the use of the Builder
    private Header(Builder builder) {
        this.apiName = builder.apiName;
        this.transmissionDate = builder.transmissionDate;
        this.transmissionTime = builder.transmissionTime;
        this.institutionCode = builder.institutionCode;
        this.fintechAppNo = builder.fintechAppNo;
        this.apiServiceCode = builder.apiServiceCode;
        this.institutionTransactionUniqueNo = builder.institutionTransactionUniqueNo;
        this.apiKey = builder.apiKey;
        this.userKey = builder.userKey;
    }

    // Builder class
    public static class Builder {

        private String apiName;
        private String transmissionDate;
        private String transmissionTime;
        private String institutionCode;
        private String fintechAppNo;
        private String apiServiceCode;
        private String institutionTransactionUniqueNo;
        private String apiKey;
        private String userKey;

        public Builder apiName(String apiName) {
            this.apiName = apiName;
            return this;
        }

        public Builder institutionCode(String institutionCode) {
            this.institutionCode = institutionCode;
            return this;
        }

        public Builder fintechAppNo(String fintechAppNo) {
            this.fintechAppNo = fintechAppNo;
            return this;
        }

        public Builder apiServiceCode(String apiServiceCode) {
            this.apiServiceCode = apiServiceCode;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder userKey(String userKey) {
            this.userKey = userKey;
            return this;
        }

        public Header build() {
            LocalDateTime now = LocalDateTime.now();

            this.institutionCode = "00100";
            this.fintechAppNo = "001";
            this.transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
            this.institutionTransactionUniqueNo = generateInstitutionTransactionUniqueNo(now);
            return new Header(this);
        }

        private String generateInstitutionTransactionUniqueNo(LocalDateTime now) {
            String dateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String randomNumbers = String.format("%06d", new Random().nextInt(1000000)); // 6자리 난수 생성
            return dateTime + randomNumbers;
        }
    }

    // Static method to create a new Builder
    public static Builder builder() {
        return new Builder();
    }
}
