package com.goofy.tunabank.v1.dto.account.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteAccountResponseDto {

    private String status;

    private String refundAccountNo;

    private Double refundAmount;

}
