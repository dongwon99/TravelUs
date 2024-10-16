package com.ssafy.soltravel.v2.dto.group.response;

import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupSummaryDto {

    @Schema(description = "모임 ID", example = "1")
    private Long groupId;

    @Schema(description = "모임 계좌 번호", example = "001-234567-89")
    private String groupAccountNo;

    @Schema(description = "모임 이름", example = "Travel Group")
    private String groupName;

    @Schema(description = "모임 아이콘", example = "airplane")
    private String icon;

    @Schema(description = "모임에 연관된 MoneyBox 리스트", example = "[{\"moneyBoxId\": 1, \"balance\": 1000.0, \"currencyCode\": \"USD\"}]")
    private List<MoneyBoxDto> moneyBoxDtoList;

    public static GroupSummaryDto createFromAccountDto(TravelGroup group, List<MoneyBoxDto> moneyBoxDtoList) {

        return GroupSummaryDto.builder()
            .groupId(group.getGroupId())
            .groupAccountNo(group.getGroupAccountNo())
            .groupName(group.getGroupName())
            .icon(group.getIcon())
            .moneyBoxDtoList(moneyBoxDtoList)
            .build();
    }
}
