package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v2.domain.Enum.ExchangeType;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
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
public class TravelGroup {

  @Id
  @Column(name = "group_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long groupId;

  private String groupAccountNo;

  private LocalDate travelStartDate;

  private LocalDate travelEndDate;

  private String groupName;

  private String icon;

  @Enumerated(EnumType.STRING)
  private ExchangeType exchangeType;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Participant> participants;

  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BillingHistory> settlementHistories;

  @OneToOne(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private TargetRate targetRate;

  public static TravelGroup createGroupEntity(String accountNo, CreateGroupRequestDto requestDto) {
    TravelGroup travelGroup = TravelGroup.builder()
        .groupAccountNo(accountNo)
        .exchangeType(requestDto.getExchangeType())
        .travelStartDate(LocalDate.now().plusDays(1))
        .travelEndDate(requestDto.getTravelEndDate())
        .groupName(requestDto.getGroupName())
        .icon(requestDto.getIcon())
        .build();

    return travelGroup;
  }
}
