package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

  @Mapping(source = "currencyCode", target = "currencyCode")
  @Mapping(source = "exchangeRate", target = "exchangeRate")
  @Mapping(source = "created", target = "created")
  @Mapping(source = "exchangeMin", target = "exchangeMin")
  ExchangeRateResponseDto toExchangeRateResponseDto(ExchangeRateCacheDto cacheDto);
}