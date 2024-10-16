package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.Card;
import com.goofy.tunabank.v1.domain.CardProduct;
import com.goofy.tunabank.v1.dto.card.CardIssueResponseDto;
import com.goofy.tunabank.v1.dto.card.CardResponseDto;
import com.goofy.tunabank.v1.dto.cardproduct.CardProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardProductMapper {

  CardProductMapper INSTANCE = Mappers.getMapper(CardProductMapper.class);

  CardProductResponseDto cpToCardProductResponseDto(CardProduct cardProduct);
}
