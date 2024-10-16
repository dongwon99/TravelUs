package com.goofy.tunabank.v1.repository;


import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.history.CardHistory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardHistoryRepository {

    private final EntityManager em;

    public Optional<List<CardHistory>> findByTransactionId(String transactionId) {
        List<CardHistory> result = em.createQuery(
                "select ch from CardHistory ch where ch.transactionId = :transactionId"
                , CardHistory.class)
            .setParameter("transactionId", transactionId)
            .getResultList();

        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public void save(CardHistory cardHistory) {
        em.persist(cardHistory);
    }

    public Double getTotalAmountByCurrency(String cardNo, CurrencyType currencyCode) {
        Double totalAmount = em.createQuery(
                "select sum(ch.amount) from CardHistory ch where ch.card.cardNo = :cardNo and ch.currency.currencyCode = :currencyCode", Double.class)
            .setParameter("cardNo", cardNo)
            .setParameter("currencyCode", currencyCode)
            .getSingleResult();

        return Optional.ofNullable(totalAmount).orElse(0.0);
    }
}