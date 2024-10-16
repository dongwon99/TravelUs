package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class TargetRate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "target_rate_id")
  private Long id;

  //설정 금액
  private Double amount;

  //희망 환율
  private Double rate;

  @OneToOne
  @JoinColumn(name = "group_id")
  private TravelGroup group;

  @Enumerated(EnumType.STRING)
  private SettlementStatus status;

  public static TargetRate createTargetRate(double amount, double rate, TravelGroup group) {

    TargetRate targetRate = new TargetRate();
    targetRate.amount = amount;
    targetRate.rate = rate;
    targetRate.group = group;
    targetRate.status = SettlementStatus.NOT_COMPLETED;
    return targetRate;
  }
}
