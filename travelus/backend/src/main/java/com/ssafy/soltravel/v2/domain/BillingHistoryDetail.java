package com.ssafy.soltravel.v2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class BillingHistoryDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "billing_history_datail_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "billing_history_id")
  private BillingHistory billingHistory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "participant_id")
  private Participant participant;

  //정산 총액
  private double amount;

  //남은 금액
  private double remainingAmount;

  //생성 일시
  private LocalDateTime createdAt;

  //수정 일시
  private LocalDateTime updatedAt;

  public static BillingHistoryDetail createBillingHistoryDetail(BillingHistory billingHistory, Participant participant,
      double amount, LocalDateTime now) {

    BillingHistoryDetail billingHistoryDetail = new BillingHistoryDetail();
    billingHistoryDetail.billingHistory = billingHistory;
    billingHistoryDetail.participant = participant;
    billingHistoryDetail.amount = amount;
    billingHistoryDetail.remainingAmount = amount;
    billingHistoryDetail.createdAt = now;
    billingHistoryDetail.updatedAt = now;
    return billingHistoryDetail;
  }
}
