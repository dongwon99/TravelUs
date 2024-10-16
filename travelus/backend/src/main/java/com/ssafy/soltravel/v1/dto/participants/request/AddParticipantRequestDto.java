package com.ssafy.soltravel.v1.dto.participants.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddParticipantRequestDto {

    @Schema(description = "참가자의 ID", example = "1")
    Long participantId;

    @Schema(description = "참가자의 계좌번호", example="0883473075115544")
    String participantAccountNo;
}
