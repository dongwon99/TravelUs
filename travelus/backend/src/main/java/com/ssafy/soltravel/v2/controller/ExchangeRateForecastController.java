package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.AllExchangeRateResponseData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateForecastSaveRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.service.exchange.ExchangeRateForecastService;
import com.ssafy.soltravel.v2.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange/forecast")
@RequiredArgsConstructor
public class ExchangeRateForecastController {

  private final ExchangeRateForecastService exchangeRateForecastService;

  /*
  * 예측 데이터 업데이트
  */
  @PostMapping("/update")
  public ResponseEntity updateForecast(@RequestBody ExchangeRateForecastSaveRequestDto request) {
    LogUtil.info("예측 데이터 수정 & 저장", request.toString());
    int response = exchangeRateForecastService.updatePred(request);
    return new ResponseEntity(String.format("예측 데이터 수정 완료(%s)", response), HttpStatus.OK);
  }

  /*
  * 과거 환율 데이터 업데이트(일부)
  */
  @PutMapping("/update/history")
  public ResponseEntity updateHistory(@RequestBody ExchangeRateSaveRequestDto request) {
    LogUtil.info("과거 데이터 수정", request.toString());
    int response = exchangeRateForecastService.updateHistories(request);
    return new ResponseEntity(String.format("수정 완료(%s)", response), HttpStatus.OK);
  }

  /*
  * 과거 환율 데이터 저장(전체)
  */
  @PostMapping("/save/history/all")
  public ResponseEntity saveAll(@RequestBody ExchangeRateSaveRequestDto request) {
    LogUtil.info("과거 데이터 전체 저장", request.toString());
    int response = exchangeRateForecastService.saveAllHistories(request);
    return new ResponseEntity(String.format("저장 완료(%s)", response), HttpStatus.CREATED);
  }

  /*
  * 모든 데이터 조회
  */
  @GetMapping
  public ResponseEntity getAllData() {
    LogUtil.info("모든 데이터 조회 요청");
    AllExchangeRateResponseData response = exchangeRateForecastService.findAll();
    return new ResponseEntity(response, HttpStatus.OK);
  }

}
