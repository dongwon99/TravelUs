package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.notification.NotificationDto;
import com.ssafy.soltravel.v2.dto.notification.PushNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.notification.RegisterNotificationRequestDto;
import com.ssafy.soltravel.v2.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "FCM 토큰 프론트 저장", description = "프론트에서 받은 FCM토큰을 Redis에 저장하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 저장 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/fcmToken")
    public ResponseEntity<ResponseDto> setToken(@RequestBody RegisterNotificationRequestDto requestDto) {
        ResponseDto responseDto = notificationService.saveFcmToken(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "FCM 토큰 삭제", description = "특정 유저의 FCM 토큰을 Redis에서 삭제하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 삭제 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @DeleteMapping("/fcmToken/{userId}")
    public ResponseEntity<ResponseDto> deleteToken(
        @PathVariable Long userId
    ) {
        ResponseDto responseDto = notificationService.deleteFcmToken(userId);

        return ResponseEntity.ok().body(responseDto);
    }


    /*
     * 알림 전송 & DB 저장
     */
    @Operation(summary = "FCM 알림 전송", description = "FCM을 통해 알림을 전송하고 DB에 저장하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "알림 전송 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/push")
    public ResponseEntity<?> pushFcmNotification(
        @RequestBody PushNotificationRequestDto requestDto) {

        ResponseEntity<?> response = notificationService.pushNotification(requestDto);

        return ResponseEntity.ok().body(response);
    }

    /*
     * 알림 조회 (특정 유저 모든 알람 조회)
     */
    @Operation(summary = "특정 유저의 알림 조회", description = "특정 유저의 모든 알림을 조회하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "알림 조회 성공", content = @Content(schema = @Schema(implementation = NotificationDto.class))),
        @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<List<NotificationDto>> getUserNotification() {

        List<NotificationDto> response = notificationService.getAllByUserId();

        return ResponseEntity.ok().body(response);
    }

    /*
     * 알림 전체 읽음 처리
     */
    @Operation(summary = "알림 전체 읽음 처리", description = "유저의 모든 알림을 읽음 처리하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "읽음 처리 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PutMapping("/read-all")
    public ResponseEntity<ResponseDto> readAllNotification() {

        ResponseDto response = notificationService.readAllNotifications();

        return ResponseEntity.ok().body(response);
    }

    /*
     * 특정 알림 삭제
     */
    @Operation(summary = "특정 알림 삭제 처리", description = "유저의 모든 알림을 삭제 처리하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "읽음 처리 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ResponseDto> deleteNotification(@PathVariable Long notificationId) {

        ResponseDto response = notificationService.deleteNotification(notificationId);

        return ResponseEntity.ok().body(response);
    }

}
