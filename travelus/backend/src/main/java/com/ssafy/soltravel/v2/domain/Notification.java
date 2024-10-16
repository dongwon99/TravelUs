package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.NotificationType;
import com.ssafy.soltravel.v2.dto.notification.PushNotificationRequestDto;
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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String title;

    private String message;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    private String icon;

    private String accountNo;

    private boolean isRead;

    private Long settlementId;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private TravelGroup group;


    public static Notification createNotification(User user, TravelGroup group, PushNotificationRequestDto requestDto) {

        return Notification.builder()
            .user(user)
            .notificationType(requestDto.getNotificationType())  // 트랜잭션 타입 설정
            .title(requestDto.getTitle())  // 알림 제목
            .message(requestDto.getMessage())  // 알림 메시지
            .icon(requestDto.getIcon())  // 알림 아이콘
            .group(group)  // 그룹 ID (null 가능)
            .accountNo(requestDto.getAccountNo())  // 계좌 번호 (null 가능)
            .isRead(false)  // 처음 알림이 생성될 때는 읽지 않은 상태로 설정
            .currencyType(requestDto.getCurrencyType())
            .settlementId(requestDto.getSettlementId())
            .build();
    }
}
