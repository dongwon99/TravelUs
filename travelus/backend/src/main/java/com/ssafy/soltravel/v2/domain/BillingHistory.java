package com.ssafy.soltravel.v2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "billing_history_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private TravelGroup group;

  @OneToMany(mappedBy = "billingHistory" , fetch = FetchType.LAZY , orphanRemoval = true)
  private List<BillingHistoryDetail> billingHistoryDetails;

  //정산 총액
  private double amount;

  //남은 금액
  private double remainingAmount;

  //생성 일시
  private LocalDateTime createdAt;

  //수정 일시
  private LocalDateTime updatedAt;

  public static BillingHistory createBillingHistory(TravelGroup group, double amount, LocalDateTime now) {
    BillingHistory billingHistory = new BillingHistory();
    billingHistory.group = group;
    billingHistory.amount = amount;
    billingHistory.remainingAmount = amount;
    billingHistory.createdAt = now;
    billingHistory.updatedAt = now;
    return billingHistory;
  }
}
