package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.Currency;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    // Currency 엔티티의 currencyCode를 기준으로 조회하는 메서드 정의
    Optional<Currency> findFirstByCurrencyCode(String currencyCode);
}
