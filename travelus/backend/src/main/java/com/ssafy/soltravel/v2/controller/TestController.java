package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.service.exchange.ExchangeBatchTriggerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TestController API", description = "데모 시연용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final ExchangeBatchTriggerService exchangeBatchTriggerService;

    @Operation(summary = "여행 DDay 알림 수행", description = "여행 7일 전 & 미환전 시 알림 전송")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "알림 전송 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping()
    public ResponseEntity<ResponseDto> travelNotification() {

        exchangeBatchTriggerService.sendTravelNotification();

        return ResponseEntity.ok().body(new ResponseDto());
    }


}
