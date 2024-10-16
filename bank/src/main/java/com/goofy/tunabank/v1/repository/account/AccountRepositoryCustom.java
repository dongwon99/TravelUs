package com.goofy.tunabank.v1.repository.account;

import com.goofy.tunabank.v1.domain.Account;
import java.util.List;
import java.util.Optional;

public interface AccountRepositoryCustom {

    Optional<Account> findByAccountNo(String accountNo);

    Optional<List<Account>> findAllAccountsByUserId(Long userId);

}
