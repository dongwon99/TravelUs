package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.history.CardHistory;
import com.goofy.tunabank.v1.dto.card.CardPaymentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardHistoryMapper {

    CardHistoryMapper INSTANCE = Mappers.getMapper(CardHistoryMapper.class);

    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "merchant.name", target = "merchantName")
    @Mapping(source = "merchant.category", target = "category")
    @Mapping(source = "transactionAt", target = "paymentAt")
    @Mapping(source = "currency.currencyCode", target = "currencyCode")
    @Mapping(source = "amount", target = "paymentBalance")
    CardPaymentResponseDto toCardPaymentResponseDto(CardHistory cardHistory);
}

