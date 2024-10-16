package com.ssafy.soltravel.v1.repository;

import com.ssafy.soltravel.v1.domain.ForeignAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ForeignAccountRepository extends JpaRepository<ForeignAccount, Long> {

    @Transactional
    void deleteByAccountNo(String accountNo);

    Optional<ForeignAccount> findByAccountNo(String accountNo);

    @Query("SELECT fa FROM ForeignAccount fa WHERE fa.generalAccount.user.userId = :userId")
    List<ForeignAccount> findAllByUserId(@Param("userId") Long userId);

}
