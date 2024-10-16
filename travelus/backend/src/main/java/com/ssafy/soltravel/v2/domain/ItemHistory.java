package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v2.dto.account_book.AccountHistorySaveRequestDto;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistorySaveRequestDto.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "item_history")
public class ItemHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_history_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cash_history_id")
  private CashHistory cashHistory;

  @Column(name = "item_name")
  private String name;

  private Double price;

  private Long quantity;

  public static List<ItemHistory> createItemHistoryList(
      CashHistory newHistory,
      AccountHistorySaveRequestDto requestDto
  ) {
    List<ItemHistory> itemHistoryList = new ArrayList<>();
    List<Item> items = requestDto.getItems();
    for(Item item : items) {
      ItemHistory itemHistory = new ItemHistory();
      itemHistory.name = item.getItem();
      itemHistory.price = item.getPrice();
      itemHistory.quantity = Long.valueOf(item.getQuantity());
      itemHistoryList.add(itemHistory);
    }

    newHistory.addAllItemHistory(itemHistoryList);
    return itemHistoryList;
  }

  public void setCashHistory(CashHistory cashHistory) {
    this.cashHistory = cashHistory;
  }
}
