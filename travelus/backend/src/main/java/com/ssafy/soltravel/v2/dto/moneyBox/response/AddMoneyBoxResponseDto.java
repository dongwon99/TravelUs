package com.ssafy.soltravel.v2.dto.moneyBox.response;

import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddMoneyBoxResponseDto {

    private Long groupId;

    private List<MoneyBoxDto> moneyBoxList;

}
