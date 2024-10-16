package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.common.RecWrapper;
import com.goofy.tunabank.v1.dto.exchange.ExchangeRateCacheDTO;
import com.goofy.tunabank.v1.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
public class ExchangeController {

  private final ExchangeService exchangeService;

  /**
   * 테스트용입니다. 삭제할 예정
   */
  @GetMapping()
  public ResponseEntity<?> getExchangeRateAll() {

    return ResponseEntity.ok().body(exchangeService.updateExchangeRates());
  }

  @PostMapping("/{currencyCode}")
  public ResponseEntity<RecWrapper<ExchangeRateCacheDTO>> getExchangeRatebyCurrencyCode(@PathVariable String currencyCode) {

    return ResponseEntity.ok().body(new RecWrapper<>(exchangeService.getExchangeRateCache(currencyCode)));
  }
}
