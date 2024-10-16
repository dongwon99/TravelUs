package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 관련 API")
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * 메시지 알림 구독
   */
  @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @Operation(summary = "메시지 알림 구독", description = "특정 사용자의 메시지 알림을 구독합니다. SSE를 통해 실시간 알림을 수신합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 알림을 구독했습니다.", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public SseEmitter subscribe(@Parameter(description = "사용자의 userId", example = "1")@PathVariable Long userId) {

    return notificationService.subscribe(userId);
  }

  @GetMapping(value = "/sendAll", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @Operation(summary = "전체 사용자에게 메세지 전송", description = "테스트용 메서드", responses = {
      @ApiResponse(responseCode = "200", description = "전송 성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<?> subscribe() {
    notificationService.notifyAllUser();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE).build();
  }
}
