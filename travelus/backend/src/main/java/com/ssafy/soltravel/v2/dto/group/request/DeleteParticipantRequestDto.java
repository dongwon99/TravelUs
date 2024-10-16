package com.ssafy.soltravel.v2.dto.group.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteParticipantRequestDto {

//    @Schema(description = "모임 ID", example = "1")
//    private Long groupId;

    @Schema(description = "참여자의 참여 Id", example = "2")
    private Long participantId;

    public static DeleteParticipantRequestDto createDto(Long participantId) {

        return DeleteParticipantRequestDto.builder()
//            .groupId(groupId)
            .participantId(participantId)
            .build();
    }

}
