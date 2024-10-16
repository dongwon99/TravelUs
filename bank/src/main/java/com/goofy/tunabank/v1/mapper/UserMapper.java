package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.user.UserJoinResponseDto;
import com.goofy.tunabank.v1.dto.user.UserSearchRequestDto;
import com.goofy.tunabank.v1.dto.user.UserSearchResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(source = "createdUser.credentialId", target = "userId")
  @Mapping(source = "createdKey.value", target = "userKey")
  @Mapping(source = "createdUser.createdAt", target = "created")
  @Mapping(source = "createdUser.updateAt", target = "modified")
  UserJoinResponseDto toUserJoinResponseDto(User createdUser, Key createdKey);

  @Mapping(source = "user.credentialId", target = "userId")
  @Mapping(source = "key.value", target = "userKey")
  @Mapping(source = "user.createdAt", target = "created")
  @Mapping(source = "user.updateAt", target = "modified")
  UserSearchResponseDto toUserSearchResponseDto(User user, Key key);
}

