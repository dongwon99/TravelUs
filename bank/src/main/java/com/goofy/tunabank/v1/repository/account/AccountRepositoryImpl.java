package com.goofy.tunabank.v1.repository.account;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.QAccount;
import com.goofy.tunabank.v1.domain.QMoneyBox;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Account> findByAccountNo(String accountNo) {
        QAccount account = QAccount.account;
        QMoneyBox moneyBox = QMoneyBox.moneyBox;
        if (accountNo == null || accountNo.isEmpty()) return Optional.empty();

        Account result = queryFactory
            .selectFrom(account)
            .leftJoin(account.moneyBoxes, moneyBox).fetchJoin()
            .where(account.accountNo.eq(accountNo)
                .and(account.status.eq("ACTIVE")
                    .and(moneyBox.status.eq("ACTIVE")))
            )
            .orderBy(moneyBox.createdAt.asc())  // createdAt을 기준으로 오름차순 정렬
            .fetchOne();

        return Optional.ofNullable(result);
    }



    @Override
    public Optional<List<Account>> findAllAccountsByUserId(Long userId) {

        QAccount account = QAccount.account;
        QMoneyBox moneyBox = QMoneyBox.moneyBox;

        List<Account> result = queryFactory.selectFrom(account)
            .leftJoin(account.moneyBoxes, moneyBox).fetchJoin()
            .where(account.user.userId.eq(userId)
                .and(account.status.eq("ACTIVE"))
                .and(moneyBox.status.eq("ACTIVE")))
            .orderBy(moneyBox.createdAt.asc())
            .fetch();

        return Optional.ofNullable(result);
    }

}
