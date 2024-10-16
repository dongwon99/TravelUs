package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.dto.card.CardPaymentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface CardMapper {
  CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

  @Mapping(source = "merchantId", target = "merchantId")
  @Mapping(source = "merchantName", target = "merchantName")
  @Mapping(source = "category", target = "category")
  @Mapping(source = "paymentAt", target = "paymentAt")
  @Mapping(source = "currencyCode", target = "currencyCode")
  @Mapping(source = "paymentBalance", target = "paymentBalance")
  CardPaymentResponseDto mapToCardPaymentResponseDto(Map<String, String> response);
}

