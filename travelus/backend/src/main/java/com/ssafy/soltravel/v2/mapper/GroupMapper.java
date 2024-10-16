package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.response.GroupSummaryDto;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface GroupMapper {

    /*
     *  그룹 매핑 메서드
     */
    @Mapping(source = "participants", target = "participants")
    GroupDto toDto(TravelGroup travelGroup);

    List<GroupDto> toDtoList(List<TravelGroup> groups);

    TravelGroup toEntity(GroupDto dto);

    GroupSummaryDto groupToSummaryDto(TravelGroup dto, List<MoneyBoxDto> moneyBoxDtoList);
}