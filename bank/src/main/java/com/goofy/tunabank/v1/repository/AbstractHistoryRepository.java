package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.history.HistoryId;
import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbstractHistoryRepository extends JpaRepository<TransactionHistory, HistoryId> {

  @Query("SELECT MAX(h.id) FROM AbstractHistory h")
  Long findMaxHistoryId();
}
