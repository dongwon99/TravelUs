package com.ssafy.soltravel.v1.dto.participants.request;

import com.ssafy.soltravel.v1.dto.participants.ParticipantDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantListResponseDto {

    @Schema(description = "계좌 ID", example = "1")
    private Long accountId;

    @Schema(description = "참가자 목록")
    private List<ParticipantDto> participants;

}
