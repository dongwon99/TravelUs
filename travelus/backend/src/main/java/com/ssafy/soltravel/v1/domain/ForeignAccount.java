package com.ssafy.soltravel.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ForeignAccount {

    @Id
    @Column(name = "foreign_account_id")
    private Long id;

    private int bankCode;

    private String accountName;

    private String accountPassword;

    private String accountNo;

    private String groupName;

    private String iconName;

    private LocalDate travelStartDate;

    private LocalDate travelEndDate;

    private Double balance;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "general_account_id")
    @JsonIgnore
    private GeneralAccount generalAccount;

    @OneToMany(mappedBy = "foreignAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountBookHistory> accountBook;

    @OneToMany(mappedBy = "foreignAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashHistory> cashHistory;

    /*
    * 연관관계 편의 메서드
    */
    public void addAccountBookHistory(AccountBookHistory accountBookHistory) {
        this.accountBook.add(accountBookHistory);
        accountBookHistory.setForeignAccount(this);
    }

    public void addCashHistory(CashHistory cashHistory) {
        this.cashHistory.add(cashHistory);
        cashHistory.setForeignAccount(this);
    }
}
