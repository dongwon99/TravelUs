package com.ssafy.soltravel.v2.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.soltravel.v2.domain.QUser;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.user.UserSearchRequestDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public long save(User user) {
    em.persist(user);
    return user.getUserId();
  }

  public Optional<User> findGroupMasterByGroupIdAndUserId(Long groupId, Long userId){
    List<User> result = em.createQuery(
        "SELECT u From User u " +
            "join u.participants p " +
            "join p.group g " +
            "where u.userId = :userId " +
            "and g.groupId = :groupId " 
//            + "and p.isMaster = true"
        )
        .setParameter("userId", userId)
        .setParameter("groupId", groupId)
        .getResultList();
    return result.stream().findFirst();
  }

  public Optional<User> findByName(String name) {
    List<User> result = em.createQuery("select u from User u where u.name = :name", User.class)
        .setParameter("name", name)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<User> findByEmail(String email) {
    List<User> result = em.createQuery("select u from User u where u.email = :email", User.class)
        .setParameter("email", email)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<User> findByUserId(Long userId) {
    List<User> result = em.createQuery("select u from User u where u.userId = :userId", User.class)
        .setParameter("userId", userId)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<User> findByEmailAndPwd(String email, String password) {
    List<User> result =
        em.createQuery("select u from User u "
                + "where u.email = :email and u.password = :password", User.class)
            .setParameter("email", email)
            .setParameter("password", password)
            .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<List<User>> findAll(UserSearchRequestDto searchDto) {
    QUser qUser = new QUser("u");
    List<User> list = queryFactory
        .selectFrom(qUser)
        .where(
            userIdEq(qUser, searchDto.getUserId()),
            userNameLike(qUser, searchDto.getName()),
            userEmailEq(qUser, searchDto.getId())
        )
        .limit(1000)
        .fetch();
    return Optional.of(list);
  }

  private BooleanExpression userIdEq(QUser user, Long userIdCond) {
    if (userIdCond == null) {
      return null;
    }
    return user.userId.eq(userIdCond);
  }

  private BooleanExpression userNameLike(QUser user, String userNameCond) {
    if (userNameCond == null) {
      return null;
    }
    return user.name.like(userNameCond);
  }

  private BooleanExpression userEmailEq(QUser user, String userEmailCond) {
    if (userEmailCond == null) {
      return null;
    }
    return user.email.eq(userEmailCond);
  }
}
