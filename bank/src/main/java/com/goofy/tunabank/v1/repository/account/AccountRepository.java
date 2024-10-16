package com.goofy.tunabank.v1.repository.account;

import com.goofy.tunabank.v1.domain.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    @Query("SELECT a FROM Account a JOIN FETCH a.moneyBoxes mb WHERE a.accountNo = :accountNo AND a.status = 'ACTIVE' AND mb.status = 'ACTIVE'")
    Optional<Account> findByAccountNo(@Param("accountNo") String accountNo);

    @Query("SELECT a FROM Account a JOIN FETCH a.moneyBoxes mb WHERE a.id = :accountId AND a.status = 'ACTIVE' AND mb.status = 'ACTIVE'")
    Optional<Account> findById(@Param("accountId") Long accountId);

    @Query("SELECT a.accountPassword FROM Account a WHERE a.accountNo = :accountNo")
    Optional<String> findPasswordByAccountNo(@Param("accountNo") String accountNo);
}