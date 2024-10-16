package com.ssafy.soltravel.v2.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Participant {

  @Id
  @Column(name = "participant_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private boolean isMaster;

  private String personalAccountNo;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_id")
  private TravelGroup group;

  @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BillingHistoryDetail> billingHistoryDetails;

  // 생성 메서드
  public static Participant createParticipant(User user, TravelGroup group, boolean isMaster, String personalAccountNo) {
    Participant participant = Participant.builder()
        .user(user)
        .group(group)
        .isMaster(isMaster)
        .personalAccountNo(personalAccountNo)
        .build();
    return participant;
  }
}