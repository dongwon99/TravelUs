package com.ssafy.soltravel.v2.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssafy.soltravel.v2.domain.Enum.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Schema(description = "알림 정보를 담고 있는 DTO")
@JsonInclude(Include.NON_NULL)
public class NotificationDto {

    @Schema(description = "알림 ID", example = "1")
    private Long notificationId;

    @Schema(description = "유저 ID", example = "123")
    private Long userId;

    @Schema(description = "알림 제목", example = "새로운 메시지가 도착했습니다.")
    private String title;

    @Schema(description = "알림 내용", example = "당신의 계좌로 새로운 입금이 발생했습니다.")
    private String message;

    @Schema(description = "알림 타입", example = "PT")
    private NotificationType notificationType;

    @Schema(description = "그룹 ID", example = "2", nullable = true)  // 그룹 ID는 선택적으로 존재
    private Long groupId;

    @Schema(description = "발생 발생 계좌", example = "001-1637835-209")
    private String accountNo;

    @Schema(description = "정산 ID", example = "3")
    private String settlementId;

    @Schema(description = "currencyCode", example = "KRW")
    private String currencyCode;

    @Schema(description = "읽음 여부", example = "false")
    private boolean isRead;

    @Schema(description = "알림 생성 시간", example = "2023-10-04T13:00:00")
    private LocalDateTime createdAt;
}