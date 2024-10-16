package com.ssafy.soltravel.v1.repository;

import com.ssafy.soltravel.v1.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {
    ExchangeRate findByCurrencyCode(String currencyCode);
}
