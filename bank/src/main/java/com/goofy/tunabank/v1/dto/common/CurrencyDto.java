package com.goofy.tunabank.v1.dto.common;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyDto {
    private Long currencyId;

    private CurrencyType currencyType;

    // 환율 추후 추가 예정
}
