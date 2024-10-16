package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.dto.moneyBox.MoneyBoxDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoneyBoxMapper {

    @Mapping(source = "id", target = "moneyBoxId")
    @Mapping(source = "currency.currencyCode", target = "currencyCode")
    MoneyBoxDto toDto(MoneyBox moneyBox);

    List<MoneyBoxDto> toDtoList(List<MoneyBox> moneyBoxes);

}
