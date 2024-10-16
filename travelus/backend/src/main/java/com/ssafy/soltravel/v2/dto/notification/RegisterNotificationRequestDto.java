package com.ssafy.soltravel.v2.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterNotificationRequestDto {

    @NotBlank(message = "토큰을 입력해야 합니다.")
    @Schema(description = "FCM Token", example = "")
    String fcmToken;
}