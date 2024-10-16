package com.ssafy.soltravel.v1.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDto {
	private String currencyCode;
	private String currencyName;
}