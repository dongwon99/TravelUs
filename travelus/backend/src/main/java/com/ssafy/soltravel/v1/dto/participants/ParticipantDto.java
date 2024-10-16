package com.ssafy.soltravel.v1.dto.participants;


import com.ssafy.soltravel.v1.dto.user.UserProfileResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {

    @Schema(description = "참가자 ID", example = "1")
    private Long participantId;

    @Schema(description = "참가자에 대한 사용자 정보")
    private UserProfileResponseDto userInfo;

    @Schema(description = "계좌 소유자 여부", example = "true")
    private boolean isMaster;

    @Schema(description = "참가자 생성 날짜 및 시간", example = "2024-08-25T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "참가자 정보 마지막 수정 날짜 및 시간", example = "2024-08-26T15:45:00")
    private LocalDateTime updatedAt;
}
