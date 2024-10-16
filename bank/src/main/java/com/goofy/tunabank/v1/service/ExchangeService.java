package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.config.RabbitMQConfig;
import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.dto.exchange.ExchangeAmountRequestDto;
import com.goofy.tunabank.v1.dto.exchange.ExchangeRateCacheDTO;
import com.goofy.tunabank.v1.exception.exchange.MinimumAmountNotSatisfiedException;
import com.goofy.tunabank.v1.exception.exchange.UnsupportedCurrencyException;
import com.goofy.tunabank.v1.repository.CurrencyRepository;
import com.goofy.tunabank.v1.util.LogUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeService {

  private final WebClient ExchangewebClient;
  private final CurrencyRepository currencyRepository;
  private final RabbitTemplate rabbitTemplate;
  private List<String> desiredCurrencies = List.of("USD", "JPY", "EUR", "TWD");


  //  @PostConstruct
//  @Scheduled(cron = "30 0 * * * ?")
  public List<ExchangeRateCacheDTO> updateExchangeRates() {
    String url = "/latest/KRW";

    String response = ExchangewebClient.get().uri(url).retrieve().bodyToMono(String.class)
        .block();

    // JSON 파싱
    JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

    //update 시간 파싱
    String timeLastUpdateUtcRaw = jsonObject.get("time_last_update_utc").getAsString();
    DateTimeFormatter inputFormatter = DateTimeFormatter.RFC_1123_DATE_TIME;
    ZonedDateTime utcZonedDateTime = ZonedDateTime.parse(timeLastUpdateUtcRaw, inputFormatter);
    ZonedDateTime seoulZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
    LocalDateTime localDateTime = seoulZonedDateTime.toLocalDateTime();
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String timeLastUpdateKst = localDateTime.format(outputFormatter);

    JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

    // 결과를 저장할 리스트
    List<ExchangeRateCacheDTO> cacheDTOList = new ArrayList<>();

    for (String currencyCode : desiredCurrencies) {
      if (conversionRates.has(currencyCode)) {
        double rate = conversionRates.get(currencyCode).getAsDouble();

        // JPY는 100 단위로 계산
        if (currencyCode.equals("JPY")) {
          rate = Math.round((1 / rate) * 10000.0) / 100.0;  // 소수점 둘째 자리까지 반올림
        } else {
          rate = Math.round((1 / rate) * 100.0) / 100.0;    // 다른 통화는 소수점 둘째 자리까지 반올림
        }

        int exchangeMin = getMinimumAmount(getCurrencyType(currencyCode));
        // DTO 객체 생성 후 리스트에 추가
        ExchangeRateCacheDTO dto = new ExchangeRateCacheDTO(currencyCode, rate, timeLastUpdateKst,
            exchangeMin);
        cacheDTOList.add(dto);

        // 환율 업데이트
        saveOrUpdateCurrency(getCurrencyType(currencyCode), rate, timeLastUpdateKst);

        // RabbitMQ로 메시지 전송
        String message = String.format("Currency: %s, Rate: %f, Time: %s, ExchangeMin: %d",
            currencyCode, rate, timeLastUpdateKst, exchangeMin);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_RATE_QUEUE, message);
        LogUtil.info("Sent message to TravelUs: {}", message);
      }
    }
    return cacheDTOList;
  }

  /**
   * 통화 단건조회(by Code)
   */
  @Transactional(readOnly = true)
  public Currency getExchangeRateByCurrencyCode(String currencyCode) {

    Currency currency = currencyRepository.findByCurrencyCode(getCurrencyType(currencyCode));
    return currency;
  }

  /**
   * 환율 단건조회(by code)
   */
  @Transactional(readOnly = true)
  public double getExchangeRateByCurrencyCode(CurrencyType currencyCode) {

    Currency currency = currencyRepository.findByCurrencyCode(currencyCode);
    return currency.getExchangeRate();
  }

  /**
   * TravelUs 캐싱용 DTO반환
   */
  public ExchangeRateCacheDTO getExchangeRateCache(String currencyCode) {

    Currency response = getExchangeRateByCurrencyCode(currencyCode);

    return ExchangeRateCacheDTO.builder()
        .CurrencyCode(currencyCode)
        .ExchangeRate(response.getExchangeRate())
        .created(response.getUpdatedAt())
        .exchangeMin(response.getExchangeMin())
        .build();
  }

  /**
   * 환율 업데이트
   */
  private void saveOrUpdateCurrency(CurrencyType type, Double exchangeRate,
      String timeLastUpdateUtc) {

    Currency existingCurrency = currencyRepository.findByCurrencyCode(type);

    existingCurrency.setExchangeRate(exchangeRate);
    existingCurrency.setUpdatedAt(timeLastUpdateUtc);
    currencyRepository.save(existingCurrency);
  }

  /**
   * 최소 환전 금액을 반환하는 메서드
   */
  public int getMinimumAmount(CurrencyType CurrencyCode) {
    switch (CurrencyCode) {
      case USD:
        return 100;
      case JPY:
        return 100;
      case EUR:
        return 100;
      case TWD:
        return 4000;
      default:
        throw new UnsupportedCurrencyException(CurrencyCode);
    }
  }

  /**
   * 환전 금액 계산 로직 1. 원화 -> 외화
   *
   * @param CurrencyCode: 바꿀 통화
   * @param amount:       원화의 얼마를 외화로 바꿀 것인지
   */
  public ExchangeAmountRequestDto calculateAmountFromKRWToForeignCurrency(CurrencyType CurrencyCode, double amount) {

    ExchangeAmountRequestDto responseDto = new ExchangeAmountRequestDto();
    BigDecimal krw = BigDecimal.valueOf(amount);
    BigDecimal rate = BigDecimal.valueOf(getExchangeRateByCurrencyCode(CurrencyCode));
    responseDto.setExchangeRate(rate.doubleValue());

    BigDecimal calculatedAmount;

    if (CurrencyCode == CurrencyType.JPY) {
      // JPY는 100엔 단위로 환율을 계산
      rate = rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
      calculatedAmount = krw.divide(rate, 0, RoundingMode.UP); // JPY는 소수점 없이 처리
    } else {
      calculatedAmount = krw.divide(rate, 2, RoundingMode.UP); // 다른 통화는 소수점 2자리까지 처리
    }

    double DcalculatedAmount = calculatedAmount.doubleValue();
    // 최소 환전 금액 유효성 검사
    if (DcalculatedAmount < getMinimumAmount(CurrencyCode)) {
      throw new MinimumAmountNotSatisfiedException(CurrencyCode, DcalculatedAmount);
    }
    responseDto.setAmount(DcalculatedAmount);
    return responseDto;
  }


  /**
   * 환전 금액 계산 로직 2. 외화 -> 원화
   */
  public ExchangeAmountRequestDto calculateAmountFromForeignCurrencyToKRW(CurrencyType CurrencyCode,
      double amount) {

    double exchangeRate = getExchangeRateByCurrencyCode(CurrencyCode);
    double krwAmount = amount * exchangeRate;

    return new ExchangeAmountRequestDto(Math.ceil(krwAmount), exchangeRate);
  }


  /**
   * String의 currencyCode를 CurreucyType으로 변환
   */
  private CurrencyType getCurrencyType(String currencyCode) {

    return switch (currencyCode) {
      case "USD" -> CurrencyType.USD;
      case "JPY" -> CurrencyType.JPY;
      case "EUR" -> CurrencyType.EUR;
      case "TWD" -> CurrencyType.TWD;
      default -> CurrencyType.KRW;
    };
  }
}