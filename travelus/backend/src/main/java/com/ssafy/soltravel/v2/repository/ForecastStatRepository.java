package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ForecastStat;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ForecastStatRepository {

  private final EntityManager em;

  public void save(ForecastStat stat){
    em.persist(stat);
  }

  public Optional<ForecastStat> findByDateAndCurrency(LocalDate date, CurrencyType currency){
    List<ForecastStat> result =  em.createQuery(
        "select fs from ForecastStat fs " +
            "where fs.date = :date " +
            "and fs.currency = :currency", ForecastStat.class
    )
        .setParameter("date", date)
        .setParameter("currency", currency)
        .getResultList();

    return result.stream().findFirst();
  }
}
