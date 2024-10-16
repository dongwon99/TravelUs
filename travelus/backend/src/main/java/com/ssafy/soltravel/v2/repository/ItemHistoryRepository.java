package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.ItemHistory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemHistoryRepository {

  private final EntityManager em;

  public void save(ItemHistory itemHistory) {
    em.persist(itemHistory);
  }

  public void saveAll(List<ItemHistory> itemHistoryList) {
    itemHistoryList.stream().forEach(history -> save(history));
  }
}
