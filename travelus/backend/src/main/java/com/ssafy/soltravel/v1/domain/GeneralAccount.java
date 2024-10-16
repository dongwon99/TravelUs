package com.ssafy.soltravel.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.soltravel.v1.domain.Enum.AccountType;
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

    private int bankCode;

    private String accountNo;

    private String accountName;

    private String accountPassword;

    private Double balance;

    private String iconName;

    private String groupName;

    private LocalDate travelStartDate;

    private LocalDate travelEndDate;

    private int countryId;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "generalAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    @OneToOne(mappedBy = "generalAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private ForeignAccount foreignAccount;

    private Float preferenceRate;
}
