package com.goofy.tunabank.v1.domain.history;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("T")
public class TransactionHistory extends AbstractHistory {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "money_box_id")
    private MoneyBox moneyBox;

    @Column(name = "transaction_account_no")
    private String transactionAccountNo;

    private String summary;

    /**
     * 엔티티 생성 메서드
     */
    public static TransactionHistory createTransactionHistory(Long nextId,
        TransactionType transactionType, MoneyBox moneyBox, String transactionAccountNo,
        LocalDateTime transactionAt, double amount, double balance, String summary) {

        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.id = nextId;
        transactionHistory.transactionType = transactionType;
        transactionHistory.moneyBox = moneyBox;
        transactionHistory.transactionAccountNo = transactionAccountNo;
        transactionHistory.transactionAt = transactionAt;
        transactionHistory.amount = amount;
        transactionHistory.balance = balance;
        transactionHistory.summary = summary;
        return transactionHistory;
    }
}
