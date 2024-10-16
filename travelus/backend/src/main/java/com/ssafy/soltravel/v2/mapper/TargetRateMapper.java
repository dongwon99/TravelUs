package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.TargetRate;
import com.ssafy.soltravel.v2.dto.targetRate.TargetRateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TargetRateMapper {

  @Mapping(target = "targetRateId", source = "id")
  TargetRateDto toTargetRateDto(TargetRate targetRate);
}
