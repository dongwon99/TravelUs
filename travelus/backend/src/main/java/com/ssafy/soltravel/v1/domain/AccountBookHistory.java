package com.ssafy.soltravel.v1.domain;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity
@Table(name = "account_book")
public class AccountBookHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long account_book_id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "foreign_account_id")
  private ForeignAccount foreignAccount;

  @Column
  private Double amount;

  @Column(name = "transaction_type")
  private String transactionType;

  @Column(name = "transaction_at")
  private LocalDateTime transactionAt;

  @Column(name = "balance")
  private Double balance;

  @Column(name = "store")
  private String store;


  public void setForeignAccount(ForeignAccount foreignAccount) {
    this.foreignAccount = foreignAccount;
  }

  public static AccountBookHistory createAccountBookHistory(
      ForeignAccount foreignAccount,String transactionType, String store, Double amount, Double balance
  ) {
    AccountBookHistory accountBookHistory = new AccountBookHistory();
    foreignAccount.addAccountBookHistory(accountBookHistory);
    accountBookHistory.store = store;
    accountBookHistory.amount = amount;
    accountBookHistory.transactionType = transactionType;
    accountBookHistory.transactionAt = LocalDateTime.now();
    accountBookHistory.balance = balance;
    return accountBookHistory;
  }

}
