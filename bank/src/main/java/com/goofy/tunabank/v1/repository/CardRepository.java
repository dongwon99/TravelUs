package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Card;
import com.goofy.tunabank.v1.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepository {

  private final EntityManager em;

  public void save(Card card) {
    em.persist(card);
  }

  public List<Card> findByUser(User user) {
    List<Card> result = em.createQuery(
            "select c from Card  c " +
                "join fetch c.account a " +
                "join fetch a.user u " +
                "join fetch c.cardProduct cp " +
                "where u = :user", Card.class
        )
        .setParameter("user", user)
        .getResultList();
    return result;
  }

  public Optional<Card> findByCardNo(String cardNo) {
    List<Card> result = em.createQuery(
            "select c from Card  c " +
                "join fetch c.account a " +
                "join fetch a.user u " +
                "where c.cardNo = :cardNo", Card.class
        )
        .setParameter("cardNo", cardNo)
        .getResultList();
    return result.stream().findFirst();
  }

  // accountNo로 카드를 조회하는 메서드
  public Optional<Card> findByAccountNo(String accountNo) {
    try {
      Card result = em.createQuery(
              "select c from Card c " +
                  "join fetch c.account a " +
                  "where a.accountNo = :accountNo", Card.class
          )
          .setParameter("accountNo", accountNo)
          .getSingleResult();
      return Optional.of(result);
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
