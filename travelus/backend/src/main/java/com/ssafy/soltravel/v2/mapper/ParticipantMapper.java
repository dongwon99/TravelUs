package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface ParticipantMapper {

    @Mapping(source = "id", target = "participantId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.profile", target = "profile")
    ParticipantDto toParticipantDto(Participant participant);

    List<ParticipantDto> toParticipantDtoList(List<Participant> participants);

}
