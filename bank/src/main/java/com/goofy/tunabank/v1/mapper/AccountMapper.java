package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.dto.account.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MoneyBoxMapper.class)
public interface AccountMapper {

    @Mapping(source = "id", target = "accountId")
    @Mapping(source = "moneyBoxes", target = "moneyBoxDtos")
    AccountDto toDto(Account account);

}
