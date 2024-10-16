package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import com.goofy.tunabank.v1.util.LogUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MoneyBox {

    //돈통 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "money_box_id")
    private Long id;

    //통장 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    //통화 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    //잔액
    private double balance;

    //개설 일시
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //수정 일시
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //거래 기록
    @OneToMany(mappedBy = "moneyBox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHistory> transactionHistories;

    private String status;

    // ==== 생성 메서드 ====
    public static MoneyBox createMoneyBox(Account account, Currency currency) {
        MoneyBox moneyBox = MoneyBox.builder()
            .account(account)
            .currency(currency)
            .balance(1000000.0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .status("ACTIVE")
            .build();

        return moneyBox;
    }

    // ==== 결제 ====
    public Double payment(Double amount) {
        //BigDecimal 변환
        BigDecimal currentBalance = BigDecimal.valueOf(balance);
        BigDecimal paymentAmount = BigDecimal.valueOf(amount);

        // 계산 및 업데이트
        BigDecimal newBalance = currentBalance.subtract(paymentAmount);
        LogUtil.info("잔액", newBalance);
        this.balance = newBalance.doubleValue();

        //반환
        return this.balance;
    }

    public void closeMoneyBox() {
        if (!this.status.equals("CLOSED")) {
            this.status = "CLOSED";
        }
    }

}
