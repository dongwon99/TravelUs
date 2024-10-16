package com.ssafy.soltravel.v1.repository;

import com.ssafy.soltravel.v1.domain.Enum.AccountType;
import com.ssafy.soltravel.v1.domain.GeneralAccount;
import com.ssafy.soltravel.v1.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GeneralAccountRepository extends JpaRepository<GeneralAccount, Long> {

    @Transactional
    void deleteByAccountNo(String accountNo);

    List<GeneralAccount>  findAllByUser_userId(Long userId);

    @Query("SELECT g.id FROM GeneralAccount g " +
        "JOIN g.foreignAccount f " +
        "WHERE g.accountNo = :accountNo OR f.accountNo = :accountNo")
    Long findAccountIdsByAccountNo(@Param("accountNo") String accountNo);

    Optional<GeneralAccount> findByAccountNo(String accountNo);

    @Query("SELECT ga.balance FROM GeneralAccount ga WHERE ga.id = :accountId")
    Double findBalanceByAccountId(@Param("accountId") Long accountId);

    GeneralAccount findFirstByUser_UserIdAndAccountType(Long userId, AccountType accountType);

    @Query("SELECT ga.user FROM GeneralAccount ga WHERE ga.id = :generalAccountId")
    User findUserByGeneralAccountId(@Param("generalAccountId") Long generalAccountId);

    @Query("SELECT ga FROM GeneralAccount ga JOIN ga.participants p WHERE p.user.userId = :userId " +
        "AND ga.accountType = 'GROUP' " +
        "AND p.isMaster = false")
    List<GeneralAccount> findAllByParticipantUserId(@Param("userId") Long userId);
}
