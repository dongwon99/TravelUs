package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.domain.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeyRepository {

  private final EntityManager em;

  public Optional<Key> findByKeyValue(String keyValue) {
    List<Key> result = em.createQuery("select k from Key k where k.value=:keyValue")
        .setParameter("keyValue", keyValue)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<Key> findByKeyValueWithUser(String keyValue) {
    List<Key> result = em.createQuery("select k from Key k join fetch k.user where k.value = :keyValue", Key.class)
        .setParameter("keyValue", keyValue)
        .getResultList();

    return result.stream().findFirst();
  }

  public Optional<Key> findValidUserKeyByUser(User user) {
    List<Key> result = em.createQuery(
        "select k from Key k where k.user = :user "
            + "and k.type = 'USER' "
            + "and k.status = 'ACTIVE' "
            + "and k.expireAt > current_timestamp"
            , Key.class
        )
        .setParameter("user", user)
        .getResultList();

    return result.stream().findFirst();
  }

  public Key findUserKeyByUser(User user) {
    List<Key> result = em.createQuery(
            "select k from Key k where k.user = :user "
                + "and k.type = 'USER'"
            , Key.class
        )
        .setParameter("user", user)
        .getResultList();
    return result.stream().findFirst().get();
  }

  public void save(Key key) {
    em.persist(key);
  }
}
