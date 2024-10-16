package com.ssafy.soltravel.v1.repository;

import com.ssafy.soltravel.v1.domain.LatestRate;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LatestRateRepository extends CrudRepository<LatestRate, Long> {

  @Query("SELECT lr FROM LatestRate lr WHERE lr.currency.currencyCode = :currency AND lr.postAt BETWEEN :startDt AND :endDt ORDER BY lr.postAt ASC")
  List<LatestRate> findLatestRatesByCurrencyAndDateRange(
      @Param("currency") String currency,
      @Param("startDt") LocalDate startDt,
      @Param("endDt") LocalDate endDt
  );

}
