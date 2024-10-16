package com.goofy.tunabank.v1.dto.moneyBox.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteMoneyBoxResponseDto {

    private String status;

    private String refundAccountNo;

    private Double refundAmount;

}
