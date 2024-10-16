package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.history.HistoryId;
import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionHistoryRepository extends
    JpaRepository<TransactionHistory, HistoryId>, TransactionHistoryCustomRepository {

//    @Query("SELECT MAX(t.id) FROM TransactionHistory t")
//    Long findMaxAccountId();
}
