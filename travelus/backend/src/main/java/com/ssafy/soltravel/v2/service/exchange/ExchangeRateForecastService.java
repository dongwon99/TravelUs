package com.ssafy.soltravel.v2.service.exchange;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.Trend;
import com.ssafy.soltravel.v2.domain.ExchangeRate;
import com.ssafy.soltravel.v2.domain.ForecastStat;
import com.ssafy.soltravel.v2.dto.exchange.forecast.AllExchangeRateResponseData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateForecastSaveRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.ExchangeRateData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.ExchangeRateForecastData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.ExchangeRateForecastStat;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.IntegratedForecastResponseData;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
import com.ssafy.soltravel.v2.repository.ForecastStatRepository;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateForecastService {

  private final ExchangeService exchangeService;
  private final ExchangeRateForecastRepository exchangeRateForecastRepository;

  private final static int DEFAULT_UPDATE_PERIOD = 7;
  private final ForecastStatRepository forecastStatRepository;

  //-----------------------------예측 환율 업데이트-----------------------------
  public int updatePred(ExchangeRateForecastSaveRequestDto request){
    int length = 0;

    for(String currency: request.getCurrencies().keySet()) {
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateForecastData data = request.getCurrencies().get(currency);
      Map<String, Double> forecasts = data.getForecast();

      //예측 환율 업데이트
      //사이즈가 같지 않으면 옛날 데이터라고 판단하고 그 기간만큼 데이터를 파싱하기 때문에 사이즈를 반드시 같게 보내야됨
      length += updateExchangeRateByPeriod(forecasts, currencyType, forecasts.size());

      //환율 관련 정보 저장
      saveForecastStat(currencyType, data.getTrend(), data.getStat());
    }

    return length;
  }

  //-----------------------------과거 환율 전체 저장-----------------------------
  public int saveAllHistories(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateData data = request.getCurrencies().get(currency);

      // 기간별 데이터 저장 메서드 호출
      Map<String, Double> recentRates = (Map<String, Double>) data.getRecentRates().get("3_months");
      length += saveExchangeRateByPeriod(recentRates, currencyType, recentRates.size());
    }

    return length;
  }

  //-----------------------------과거 환율 일부 업데이트-----------------------------
  public int updateHistories(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateData data = request.getCurrencies().get(currency);

      // 기간별 데이터 저장 메서드 호출
      Map<String, Double> recentRates = (Map<String, Double>) data.getRecentRates().get("3_months");
      length += updateExchangeRateByPeriod(recentRates, currencyType, DEFAULT_UPDATE_PERIOD);
    }

    return length;
  }

  // 기간별 환율 저장(공통 메서드)
  private int saveExchangeRateByPeriod(Map<String, Double> data, CurrencyType cType, int period) {
    if(period != data.size()){ //전체 데이터를 저장할경우 기간 파싱 X
      data = parsePastDataByPeriod(data, period);
    }

    for(String dateString : data.keySet()) {
      ExchangeRate rate = ExchangeRate.create(
          LocalDate.parse(dateString),
          cType,
          data.get(dateString)
      );
      exchangeRateForecastRepository.save(rate);
    }

    return period;
  }

  // 기간별 환율 수정 및 생성(공통 메서드)
  private int updateExchangeRateByPeriod(Map<String, Double> data, CurrencyType cType, int period) {
    if(period != data.size()){
      data = parsePastDataByPeriod(data, period);
    }

    for(String dateString : data.keySet()) {
      LocalDate date = LocalDate.parse(dateString);
      Double exchangeRateValue = data.get(dateString);

      ExchangeRate rate = exchangeRateForecastRepository.findByDateAndCurrency(date, cType).orElse(
          null
      );

      if(rate != null) {
        rate.updateRate(exchangeRateValue);
      }
      else{
        rate = ExchangeRate.create(
            date,
            cType,
            exchangeRateValue
        );
        exchangeRateForecastRepository.save(rate);
      }
    }

    return period;
  }

  // 데이터 기간으로 파싱(공통 메서드)
  private Map<String, Double> parsePastDataByPeriod(Map<String, Double> data, int period) {
    Map<String, Double> parsedData = new LinkedHashMap<>();
    for(int i = period; i >= 0; i--) {
      LocalDate date = LocalDate.now().minusDays(i);
      if(!data.containsKey(date.toString())) continue;

      Double rate = data.get(date.toString());
      parsedData.put(date.toString(), rate);
    }
    return parsedData;
  }

  // 환율 예측 관련 정보 저장(오늘 기준)
  private void saveForecastStat(CurrencyType cType, String trend, ExchangeRateForecastStat stat) {
    ForecastStat forecastStat = ForecastStat.createForecastStat(
        cType,
        trend,
        stat
    );
    forecastStatRepository.save(forecastStat);
  }

  //-----------------------------전체 정보 조회-----------------------------
  public AllExchangeRateResponseData findAll() {
    AllExchangeRateResponseData response = new AllExchangeRateResponseData();
    for(CurrencyType currencyType: CurrencyType.values()) {
      if(currencyType.equals(CurrencyType.KRW)) continue;

      IntegratedForecastResponseData data = findByCurrency(currencyType);
      response.setCurrency(currencyType.getCurrencyCode(), data);
    }
    return response;
  }

  //-----------------------------통화별 정보 조회-----------------------------
  private IntegratedForecastResponseData findByCurrency(CurrencyType currencyType) {

    // 환율 과거 데이터 조회
    List<ExchangeRate> recentRates_3 = findExchangeRateByPeriod(
        LocalDate.now().minusMonths(3),
        LocalDate.now(),
        currencyType,
        false
    );

    // 환율 예측 데이터 조회
    List<ExchangeRate> forecast = findExchangeRateByPeriod(
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(15),
        currencyType,
        true
    );

    // 환율 예측 관련 데이터 조회
    ForecastStat stat = findTodayForecastStatByCurrency(currencyType);

    return toIntegratedDto(
        recentRates_3,
        forecast,
        stat
    );
  }


  /*
  *
  * 내부 공통 메서드
  *
  */

  // 기간별 환율 조회
  private List<ExchangeRate> findExchangeRateByPeriod(LocalDate start, LocalDate end, CurrencyType cType, Boolean isAsc) {
    return exchangeRateForecastRepository.findByPeriodAndCurrency(start, end, cType, isAsc).orElse(
        List.of()
    );
  }

  // 오늘 환율 예측 관련 정보 조회
  private ForecastStat findTodayForecastStatByCurrency(CurrencyType currency) {
    return forecastStatRepository.findByDateAndCurrency(LocalDate.now(), currency).orElse(
        null
    );
  }

  // 통합 DTO
  private IntegratedForecastResponseData toIntegratedDto(
      List<ExchangeRate> recentRates_3,
      List<ExchangeRate> forecast,
      ForecastStat stat
  ) {
    return IntegratedForecastResponseData.builder()
        .forecast(toForecastDto(forecast))
        .recentRates(Map.of("3_months", toRecentRatesDto(recentRates_3)))
        .stat(toForecastStatDto(stat))
        .trend(stat == null ? null : stat.getTrend())
        .currentRate(recentRates_3.get(0).getRate())
        .build();
  }

  // 환율 과거 DTO
  private Map<String, Double> toRecentRatesDto(List<ExchangeRate> recentRates_3){
    Map<String, Double> recentRates = new LinkedHashMap<>();
    for(ExchangeRate recentRate: recentRates_3) {
      recentRates.put(recentRate.getDate().toString(), recentRate.getRate());
    }
    return recentRates;
  }

  // 환율 예측 DTO
  private Map<String, Double> toForecastDto(List<ExchangeRate> forecast) {
    Map<String, Double> forecastDto = new LinkedHashMap<>();
    for(ExchangeRate forecastRate: forecast) {
      forecastDto.put(forecastRate.getDate().toString(), forecastRate.getRate());
    }
    return forecastDto;
  }

  // 환율 예측 정보 DTO
  private ExchangeRateForecastStat toForecastStatDto(ForecastStat stat) {
    if(stat == null) return null;
    ExchangeRateForecastStat statDto = ExchangeRateForecastStat.builder()
        .min(stat.getMin())
        .max(stat.getMax())
        .average(stat.getAverage())
        .p10(stat.getP10())
        .p20(stat.getP20())
        .p30(stat.getP30())
        .p40(stat.getP40())
        .p50(stat.getP50())
        .p60(stat.getP60())
        .p70(stat.getP70())
        .p80(stat.getP80())
        .p90(stat.getP90())
        .p100(stat.getP100())
        .build();
    return statDto;
  }

}
