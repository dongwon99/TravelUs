package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.BillingHistory;
import com.ssafy.soltravel.v2.domain.BillingHistoryDetail;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import com.ssafy.soltravel.v2.dto.settlement.response.GroupSettlementResponseDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementHistoryDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {SettlementMapper.class})
public interface SettlementMapper {

  @Mapping(source = "id", target = "personalSettlementId")
  @Mapping(source = "createdAt", target = "settlementRequestTime")
  @Mapping(source = "billingHistoryDetails", target = "participants")
  @Mapping(source = "amount", target = "totalAmount")
  @Mapping(target = "isSettled", expression = "java(determineGroupSettlementStatus(billingHistory))")
  GroupSettlementResponseDto toGroupSettlementResponseDto(BillingHistory billingHistory);

  List<GroupSettlementResponseDto> toGroupSettlementResponseDtos(List<BillingHistory> billingHistories);

  @Mapping(source = "billingHistory.group.groupId", target = "groupId")
  @Mapping(source = "billingHistory.group.groupName", target = "groupName")
  @Mapping(source = "id", target = "settlementDetailId")
  @Mapping(source = "participant.id", target = "participantId")
  @Mapping(source = "remainingAmount", target = "isSettled", qualifiedByName = "mapSettlementStatus")
  @Mapping(source = "createdAt", target = "settlementRequestTime")
  @Mapping(target = "participantCount", expression = "java(billingHistoryDetail.getBillingHistory().getGroup().getParticipants().size())")
  @Mapping(source = "participant.user.profile", target = "profile")
  @Mapping(source = "billingHistory.id", target = "settlementId")
  @Mapping(source = "billingHistory.amount", target = "totalAmount")
  PersonalSettlementDto toPersonalSettlementDto(BillingHistoryDetail billingHistoryDetail);

  List<PersonalSettlementDto> toPersonalSettlementDtos(List<BillingHistoryDetail> billingHistoryDetails);

  @Mapping(source = "id", target = "settlementDetailId")
  @Mapping(source = "participant.id", target = "participantId")
  @Mapping(source = "participant.user.name", target = "participantName")
  @Mapping(source = "participant.user.profile", target = "profile")
  @Mapping(source = "amount", target = "amount")
  @Mapping(source = "remainingAmount", target = "remainingAmount")
  @Mapping(source = "remainingAmount", target = "isSettled", qualifiedByName = "mapSettlementStatus")
  @Mapping(source = "createdAt", target = "settlementRequestTime")
  PersonalSettlementHistoryDto toPersonalSettlementHistoryDto(BillingHistoryDetail billingHistoryDetail);

  List<PersonalSettlementHistoryDto> toPersonalSettlementHistoryDtos(List<BillingHistoryDetail> billingHistoryDetails);

  @Named("mapSettlementStatus")
  default SettlementStatus mapSettlementStatus(double remainingAmount) {
    return remainingAmount <= 0 ? SettlementStatus.COMPLETED : SettlementStatus.NOT_COMPLETED;
  }

  default SettlementStatus determineGroupSettlementStatus(BillingHistory billingHistory) {
    return billingHistory.getBillingHistoryDetails().stream()
        .allMatch(detail -> mapSettlementStatus(detail.getRemainingAmount()) == SettlementStatus.COMPLETED)
        ? SettlementStatus.COMPLETED
        : SettlementStatus.NOT_COMPLETED;
  }
}