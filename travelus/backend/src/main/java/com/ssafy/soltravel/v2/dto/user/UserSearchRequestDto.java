package com.ssafy.soltravel.v2.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class UserSearchRequestDto {

    @Null
    @Schema(description = "사용자 ID(DB 시퀀스값)", example = "1")
    private Long userId;

    @Null
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Null
    @Schema(description = "사용자 아이디(로그인 아이디)", example = "hdw")
    private String id;

}