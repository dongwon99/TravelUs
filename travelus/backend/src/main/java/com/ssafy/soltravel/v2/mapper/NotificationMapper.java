package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.Notification;
import com.ssafy.soltravel.v2.dto.notification.NotificationDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "id", target = "notificationId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "group.groupId", target = "groupId")
    @Mapping(source = "currencyType", target = "currencyCode")
    NotificationDto toDto(Notification notification);

    List<NotificationDto> toDtoList(List<Notification> notifications);

}
