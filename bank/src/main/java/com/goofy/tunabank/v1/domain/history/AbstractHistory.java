package com.goofy.tunabank.v1.domain.history;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@IdClass(HistoryId.class)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TTYPE")
@Table(name = "history")
@Getter
@ToString
public abstract class AbstractHistory {

    // 거래 기록 id
    @Id
    @Column(name = "transaction_history_id")
    protected Long id;

    // 거래 종류
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    protected TransactionType transactionType;

    // 공통 필드
    @Column(name = "transaction_at")
    protected LocalDateTime transactionAt;

    @Column(name = "amount")
    protected Double amount;

    @Column(name = "balance")
    protected Double balance;
}
