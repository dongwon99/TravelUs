package com.goofy.tunabank.v1.dto.account;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.dto.moneyBox.MoneyBoxDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {

    private Long accountId;

    private String credentialId;

    private String accountNo;

    private String accountPassword;

    private AccountType accountType;

    private int bankId;

    private List<MoneyBoxDto> moneyBoxDtos;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}