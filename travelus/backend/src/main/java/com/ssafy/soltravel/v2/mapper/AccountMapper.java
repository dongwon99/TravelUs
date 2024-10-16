package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final CurrencyRepository currencyRepository;



//
//    public AccountDto toCreateAccountDto(ForeignAccount foreignAccount) {
//
//        CurrencyDto currencyDto = new CurrencyDto(
//            foreignAccount.getCurrency().getCurrencyCode(),
//            foreignAccount.getCurrency().getCurrencyName()
//        );
//
//        AccountDto accountDto = AccountDto.builder()
//            .id(foreignAccount.getId())
//            .bankCode(foreignAccount.getBankCode())
//            .accountPassword(foreignAccount.getAccountPassword())
//            .accountName("신한은행 외화 모임통장")
//            .accountType(AccountType.GROUP)
//            .accountNo(foreignAccount.getAccountNo())
//            .groupName(foreignAccount.getGroupName())
//            .balance(foreignAccount.getBalance())
//            .travelStartDate(foreignAccount.getTravelStartDate())
//            .travelEndDate(foreignAccount.getTravelEndDate())
//            .iconName(foreignAccount.getIconName())
//            .currency(currencyDto)
//            .createdAt(String.valueOf(foreignAccount.getCreatedAt()))
//            .updatedAt(String.valueOf(foreignAccount.getUpdatedAt()))
//            .build();
//
//        return accountDto;
//    }
//
//    public ForeignAccount toForeignAccountEntitiy(
//        Map<String, String> recObject,
//        GeneralAccount generalAccount,
//        CreateAccountRequestDto requestDto
//    ) {
//
//        Currency currency = currencyRepository.findFirstByCurrencyCode(requestDto.getCurrencyCode()).orElseThrow(
//            () -> new IllegalArgumentException("Currency Does Not Exist" + requestDto.getCurrencyCode()));
//
//        ForeignAccount foreignAccount = ForeignAccount.builder()
//            .id(generalAccount.getId())
//            .bankCode(Integer.parseInt(recObject.get("bankCode")))
//            .accountNo(recObject.get("accountNo"))
//            .accountPassword(requestDto.getAccountPassword())
//            .accountName("신한은행 외화 모임통장")
//            .balance(0.0)
//            .generalAccount(generalAccount)
//            .travelStartDate(requestDto.getTravelStartDate())
//            .travelEndDate(requestDto.getTravelEndDate())
//            .currency(currency)
//            .groupName(requestDto.getGroupName())
//            .iconName(requestDto.getIconName())
//            .build();
//
//        return foreignAccount;
//    }
//
//
//    public GeneralAccount toGeneralAccountEntitiy(
//        Map<String, String> recObject,
//        User user,
//        CreateAccountRequestDto requestDto
//    ) {
//
//        ModelMapper modelMapper = new ModelMapper();
//
//        GeneralAccount generalAccount = modelMapper.map(recObject, GeneralAccount.class);
//
//        generalAccount.setBalance(0.0);
//        generalAccount.setUser(user);
//        generalAccount.setAccountType(requestDto.getAccountType());
//        generalAccount.setAccountPassword(requestDto.getAccountPassword());
//        generalAccount.setPreferenceRate(requestDto.getExchangeRate());
//
//        if(generalAccount.getAccountType().equals(AccountType.INDIVIDUAL)) {
//            generalAccount.setAccountName("신한은행 일반 개인통장");
//        }else{
//            generalAccount.setTravelStartDate(requestDto.getTravelStartDate());
//            generalAccount.setTravelEndDate(requestDto.getTravelEndDate());
//            generalAccount.setGroupName(requestDto.getGroupName());
//            generalAccount.setAccountName("신한은행 일반 모임통장");
//            generalAccount.setIconName(requestDto.getIconName());
//        }
//
//        return generalAccount;
//    }
//


}
