package com.ssafy.soltravel.v2.dto.group.request;

import com.ssafy.soltravel.v2.domain.Enum.ExchangeType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateGroupRequestDto {

    @Schema(description = "새로 만들 모임 계좌의 비밀번호", example = "1234")
    private String groupAccountPassword;

    @Schema(description = "모임의 이름", example = "TravalUs 여행 모임")
    private String groupName;

    @Schema(description = "모임의 아이콘", example = "airPlane")
    private String icon;

    @Schema(description = "여행 시작 날짜", example = "2024-01-01")
    private LocalDate travelStartDate;

    @Schema(description = "여행 종료 날짜", example = "2024-01-07")
    private LocalDate travelEndDate;

    @Schema(description = "정산 받은 개인 계좌번호", example = "002-4561621-209")
    private String personalAccountNo;

    @Schema(description = "환전 타입", example="AUTO")
    private ExchangeType exchangeType;
}


