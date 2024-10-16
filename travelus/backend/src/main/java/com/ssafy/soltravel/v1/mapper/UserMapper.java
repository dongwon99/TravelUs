package com.ssafy.soltravel.v1.mapper;

import com.ssafy.soltravel.v1.domain.User;
import com.ssafy.soltravel.v1.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.v1.dto.user.UserProfileResponseDto;

public class UserMapper {

    // 응애
    public static UserProfileResponseDto convertUserToProfileResponseDto(User user) {
        return UserProfileResponseDto.builder()
            .userId(user.getUserId())
            .username(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .address(user.getAddress())
            .birth(user.getBirth())
            .registerAt(user.getRegisterAt())
            .build();
    }

    // 회원가입 요청 DTO를 유저 엔티티로 변환
    public static User convertCreateDtoToUserWithUserKey(
        UserCreateRequestDto dto, String profileImageUrl, String userKey
    ) {
        return User.createUser(
            dto.getName(),
            dto.getPassword(),
            dto.getEmail(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getBirth(),
            profileImageUrl,
            userKey
        );
    }

    // 회원가입 요청 DTO를 유저 엔티티로 변환(신한 API X)
    public static User convertCreateDtoToUserWithUserKey(UserCreateRequestDto dto, String profileImageUrl) {
        return User.createUser(
            dto.getName(),
            dto.getPassword(),
            dto.getEmail(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getBirth(),
            profileImageUrl
        );
    }
}
