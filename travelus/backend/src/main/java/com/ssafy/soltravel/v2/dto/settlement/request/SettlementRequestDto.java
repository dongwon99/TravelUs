package com.ssafy.soltravel.v2.dto.settlement.request;

import com.ssafy.soltravel.v2.domain.Enum.SettlementType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class SettlementRequestDto {

    @Schema(description = "모임 Id", example = "1")
    private Long groupId;

    @Schema(description = "계좌번호", example = "002-45579486-209")
    private String accountNo;

    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "정산타입(G:원화만, F:외화만, BOTH:모두)", example = "BOTH")
    private SettlementType settlementType;

    @Schema(description = "전체 정산금액[원화,외화], 원화만일때는 0으로", example = "[0,100000]")
    private List<Double> amounts;

    @Schema(description = "참여자별 정산금액")
    private List<SettlementParticipantRequestDto> participants;
}
