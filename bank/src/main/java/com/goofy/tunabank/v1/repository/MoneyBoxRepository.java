package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoneyBoxRepository extends JpaRepository<MoneyBox, Long> {

    @Override
    void deleteById(Long id);

    @Query("SELECT mb FROM MoneyBox mb " +
        "JOIN FETCH mb.currency c " +
        "JOIN FETCH mb.account a " +
        "WHERE a.id = :accountId " +
        "AND c.currencyCode = :currencyCode " +
        "AND mb.status = 'ACTIVE' " +
        "AND a.status = 'ACTIVE'")
    Optional<MoneyBox> findMoneyBoxByAccountAndCurrency(@Param("accountId") Long accountId,
        @Param("currencyCode") CurrencyType currencyCode);

    @Query("SELECT mb FROM MoneyBox mb " +
        "JOIN FETCH mb.currency c " +
        "JOIN FETCH mb.account a " +
        "WHERE a.accountNo = :accountNo " +
        "AND c.currencyCode = :currencyCode " +
        "AND mb.status = 'ACTIVE' " +
        "AND a.status = 'ACTIVE'")
    Optional<MoneyBox> findMoneyBoxByAccountNoAndCurrency(@Param("accountNo") String accountNo,
        @Param("currencyCode") CurrencyType currencyCode);

}
