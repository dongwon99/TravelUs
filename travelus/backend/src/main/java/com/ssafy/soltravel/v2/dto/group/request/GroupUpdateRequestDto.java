package com.ssafy.soltravel.v2.dto.group.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
public class GroupUpdateRequestDto {


    @Schema(description = "모임 계좌 번호", required = true, example = "002-1958341-209")
    private String groupAccountNo;

    @Schema(description = "여행 시작 날짜", example = "2024-10-10")
    private LocalDate travelStartDate;

    @Schema(description = "여행 종료 날짜", example = "2024-10-12")
    private LocalDate travelEndDate;

    @Schema(description = "모임 이름", example = "TravelUs 여행 모임")
    private String groupName;

    @Schema(description = "모임 아이콘 URL", example = "/images/icon.png")
    private String icon;
}
