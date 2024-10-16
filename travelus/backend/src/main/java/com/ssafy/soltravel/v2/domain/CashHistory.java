package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v1.domain.ForeignAccount;
import com.ssafy.soltravel.v2.domain.Enum.CashTransactionType;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistorySaveRequestDto;
import com.ssafy.soltravel.v2.util.DateUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "cash_history")
public class CashHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cash_history_id;

  @Column(name = "store")
  private String store;

  @Column
  private Double amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type")
  private CashTransactionType transactionType;

  @Column(name = "transaction_at")
  private LocalDateTime transactionAt;

  @Column
  @Enumerated(EnumType.STRING)
  private CurrencyType currency;

  @Column
  private String address;

  @OneToMany(mappedBy = "cashHistory", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemHistory> itemHistoryList = new ArrayList<>();

  @Column(name = "account_no")
  private String accountNo;

  /*
  * 생성 메서드
  */
  public static CashHistory createPaidCashHistory(AccountHistorySaveRequestDto requestDto) {
    CashHistory cashHistory = new CashHistory();
    cashHistory.store = requestDto.getStore();
    cashHistory.amount = requestDto.getPaid();
    cashHistory.transactionType = CashTransactionType.P;
    cashHistory.transactionAt = DateUtil.parseTransactionAt(requestDto.getTransactionAt());
    cashHistory.currency = CurrencyType.valueOf(requestDto.getCurrency());
    cashHistory.address = requestDto.getAddress();
    cashHistory.accountNo = requestDto.getAccountNo();
    return cashHistory;
  }

  /*
  * 연관관계 편의 메서드
  */
  public void addItemHistory(ItemHistory itemHistory) {
    itemHistoryList.add(itemHistory);
    itemHistory.setCashHistory(this);
  }

  public void addAllItemHistory(List<ItemHistory> itemHistoryList) {
    this.itemHistoryList.addAll(itemHistoryList);
    itemHistoryList.stream().forEach(itemHistory -> itemHistory.setCashHistory(this));
  }

}
