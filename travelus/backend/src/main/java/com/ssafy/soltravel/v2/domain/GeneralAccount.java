package com.ssafy.soltravel.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GeneralAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "general_account_id")
    private Long id;

    private String accountNo;

    private String accountPassword;

    private int bankCode;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String accountName;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Float preferenceRate;

    public static GeneralAccount fromRecObject(Map<String, Object> recObject) {
        return GeneralAccount.builder()
            .accountNo((String) recObject.get("accountNo"))
            .accountPassword((String) recObject.get("accountPassword"))
            .accountType(AccountType.valueOf((String) recObject.get("accountType"))) // accountType은 Enum으로 변환
            .bankCode((Integer) recObject.get("bankCode"))
            .build();
    }

}
