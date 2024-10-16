package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Currency findByCurrencyCode(CurrencyType currencyCode);
}
