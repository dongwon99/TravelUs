package com.ssafy.soltravel.v2.dto.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.soltravel.v2.domain.Enum.ExchangeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
@Schema(description = "모임 정보를 담고 있는 DTO")
public class GroupDto {

    @Schema(description = "모임 ID", example = "1")
    private Long groupId;

    @Schema(description = "모임 계좌 번호", example = "001-93381440-209")
    private String groupAccountNo;

    @Schema(description = "환전 타입", example = "AUTO")
    private ExchangeType exchangeType;

    @Schema(description = "카드 번호", example = "4000001679635483")
    private String cardNumber;

    @Schema(description = "여행 시작 날짜", example = "2024-01-01")
    private LocalDate travelStartDate;

    @Schema(description = "여행 종료 날짜", example = "2024-01-07")
    private LocalDate travelEndDate;

    @Schema(description = "모임 이름", example = "TravelUs 여행 모임")
    private String groupName;

    @Schema(description = "모임 아이콘", example = "airPlane")
    private String icon;

    @Schema(description = "참여자 목록")
    private List<ParticipantDto> participants = new ArrayList<>();

    @Schema(description = "생성 일시", example = "2024-09-24T15:58:37")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2024-09-24T15:58:37")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}