package com.ssafy.soltravel.v1.mapper;

import com.ssafy.soltravel.v1.domain.Participant;
import com.ssafy.soltravel.v1.dto.participants.ParticipantDto;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ParticipantMapper {

    public static ParticipantDto toDto(Participant participant) {

        ParticipantDto participantDto = ParticipantDto.builder()
            .participantId(participant.getId())
            .userInfo(UserMapper.convertUserToProfileResponseDto(participant.getUser()))
            .isMaster(participant.isMaster())
            .createdAt(participant.getCreatedAt())
            .updatedAt(participant.getUpdatedAt())
            .build();

        return participantDto;
    }
}
