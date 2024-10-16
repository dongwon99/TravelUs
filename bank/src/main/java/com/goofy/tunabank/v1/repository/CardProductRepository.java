package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.CardProduct;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardProductRepository {

  private final EntityManager em;

  public Optional<CardProduct> findByUniqueNo(String uniqueNo) {
    List<CardProduct> result = em.createQuery(
        "select cp from CardProduct cp where cp.cardUniqueNo = :uniqueNo",
            CardProduct.class
        )
        .setParameter("uniqueNo", uniqueNo)
        .getResultList();
    return result.stream().findFirst();
  }

  public List<CardProduct> findAll() {
    return em.createQuery("select cp from CardProduct cp", CardProduct.class).getResultList();
  }

}
