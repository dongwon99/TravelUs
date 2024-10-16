package com.ssafy.soltravel.v1.service.exchange;

import com.ssafy.soltravel.v1.common.Header;
import com.ssafy.soltravel.v1.domain.ExchangeRate;
import com.ssafy.soltravel.v1.domain.ForeignAccount;
import com.ssafy.soltravel.v1.domain.GeneralAccount;
import com.ssafy.soltravel.v1.domain.LatestRate;
import com.ssafy.soltravel.v1.domain.User;
import com.ssafy.soltravel.v1.domain.redis.PreferenceRate;
import com.ssafy.soltravel.v1.dto.exchange.Account;
import com.ssafy.soltravel.v1.dto.exchange.AccountInfoDto;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeCurrencyDto;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeRateDto;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeRequestDto;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeResponseDto;
import com.ssafy.soltravel.v1.dto.exchange.LatestRateRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.request.ForeignTransactionRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v1.repository.ExchangeRateRepository;
import com.ssafy.soltravel.v1.repository.GeneralAccountRepository;
import com.ssafy.soltravel.v1.repository.LatestRateRepository;
import com.ssafy.soltravel.v1.repository.redis.PreferenceRateRepository;
import com.ssafy.soltravel.v1.service.NotificationService;
import com.ssafy.soltravel.v1.service.account.AccountService;
import com.ssafy.soltravel.v1.service.transaction.TransactionService;
import com.ssafy.soltravel.v1.util.LogUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeService {

  private final Map<String, String> apiKeys;
  private final CacheManager cacheManager;
  
  private final WebClient webClient;
  private final WebClient BankWebClient;
  
  private final ModelMapper modelMapper;
  private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu";

  private final ExchangeRateRepository exchangeRateRepository;
  private final PreferenceRateRepository preferenceRateRepository;
  private final NotificationService notificationService;
  private final AccountService accountService;
  private final TransactionService transactionService;
  private final LatestRateRepository latestRateRepository;
  private final GeneralAccountRepository generalAccountRepository;

  /**
   * 실시간 환율 받아오는 메서드 매시 0분, 10분, 20분, 30분, 40분, 50분에 data 가져온다
   */
  @Scheduled(cron = "0 0/10 * * * *")
  public void ScheduledGetExchangeRate() {

    String API_NAME = "exchangeRate";
    String API_URL = BASE_URL + "/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY")).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);

    ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        }).block();

    // REC 부분을 Object 타입으로 받기 -> List<Map<String, Object>>로 변환
    Object recObject = response.getBody().get("REC");
    List<Map<String, Object>> recList = (List<Map<String, Object>>) recObject;

    // ModelMapper를 사용하여 각 Map을 ExchangeRateDto로 변환
    List<ExchangeRateDto> responseDtoList = recList.stream()
        .map(map -> modelMapper.map(map, ExchangeRateDto.class)).collect(Collectors.toList());

    //DB 업데이트
    updateExchangeRates(responseDtoList);
  }

  /**
   * 현재 환율 전체 조회
   */
  public List<ExchangeRate> getExchangeRateAll() {

    List<ExchangeRate> rateEntity = exchangeRateRepository.findAll();

    LogUtil.info(rateEntity.toString());
    return rateEntity;
  }


  /**
   * 현재 환율 조회
   */
  public ExchangeRateResponseDto getExchangeRate(String currency) {

    ExchangeRateResponseDto responseDto = new ExchangeRateResponseDto();
    ExchangeRate rateEntity = exchangeRateRepository.findByCurrencyCode(currency);

    responseDto.setCurrencyCode(currency);
    responseDto.setExchangeRate(rateEntity.getExchangeRate());
    responseDto.setExchangeMin(rateEntity.getExchangeMin());
    return responseDto;
  }

  /**
   * 최근 환율 조회
   */
  public List<LatestRate> getLatestExchangeRate(LatestRateRequestDto requestDto) {

    return latestRateRepository.findLatestRatesByCurrencyAndDateRange(requestDto.getCurrencyCode(),
        requestDto.getStartDate(), requestDto.getEndDate());
  }

  /**
   * 실시간 환율 데이터 db에 저장
   */
  public void updateExchangeRates(List<ExchangeRateDto> dtoList) {
    for (ExchangeRateDto dto : dtoList) {

      ExchangeRate rate = exchangeRateRepository.findByCurrencyCode(dto.getCurrency());
      double prevRate = -1D;

      if (rate != null) {
        //이전 환율 저장
        prevRate = rate.getExchangeRate();
      } else {
        rate = ExchangeRate.builder()
            .currencyCode(dto.getCurrency())
            .build();
      }

      double updatedRate = getDoubleExchangeRate(dto.getExchangeRate());

      rate = rate.toBuilder()
          .exchangeRate(updatedRate)
          .exchangeMin(Double.parseDouble(dto.getExchangeMin()))
          .created(getLocalDateTime(dto.getCreated()))
          .build();
      exchangeRateRepository.save(rate);

      /**
       * 환율이 변동되었다 -> 자동 환전
       */
      if (prevRate != updatedRate) {
        // ID에 등록된 account를 가져온다
        String id = makeId(dto.getCurrency(), updatedRate);
        Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

        if (exchangeOpt.isPresent()) {
          PreferenceRate preferenceRate = exchangeOpt.get();

          for (Account account : preferenceRate.getAccounts()) {

            GeneralAccount generalAccount = generalAccountRepository.findById(
                account.getAccountId()).orElseThrow();

            ExchangeRequestDto exchangeRequestDto = ExchangeRequestDto.builder()
                .currencyCode(dto.getCurrency())
                .accountId(account.getAccountId())
                .accountNo(account.getAccountNo())
                .exchangeAmount(Math.round(generalAccount.getBalance()))//모임계좌 전액 환전
                .exchangeRate(updatedRate)
                .build();

            LogUtil.info("<<자동 환전>> 모임 계좌 번호:", generalAccount.getAccountNo());
            LogUtil.info("<<자동 환전>> 계좌 잔액:", generalAccount.getBalance());
            executeKRWTOUSDExchange(exchangeRequestDto);
          }
        }
      }
    }
  }

  /**
   * 환전 선호 금액 설정
   */
  public void setPreferenceRate(String accountNo, ExchangeRateRegisterRequestDto dto) {

    String id = makeId(dto.getCurrencyCode(), dto.getExchangeRate());
    Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

    PreferenceRate preference;
    if (exchangeOpt.isPresent()) {
      preference = exchangeOpt.get();
    } else {
      preference = new PreferenceRate(id, new HashSet<>());
    }

    preference.getAccounts().add(new Account(dto.getGeneralAccountId(), accountNo));
    preferenceRateRepository.save(preference);
  }

  /**
   * 원화 -> USD 환전
   */
  public ExchangeResponseDto executeKRWTOUSDExchange(ExchangeRequestDto dto) {

    double krw = dto.getExchangeAmount();
    LogUtil.info("<<환전>> 요청된 원화 금액:", dto.getExchangeAmount());

    if (dto.getExchangeAmount() % 10 != 0) {
      dto.setExchangeAmount(krw - krw % 10);
    }

    dto.setExchangeRate(
        exchangeRateRepository.findByCurrencyCode(dto.getCurrencyCode()).getExchangeRate());

    LogUtil.info("<<환전>> 적용되는 환율:", dto.getExchangeRate());
    /**
     * 원화 -> 달러
     */
    double amount = convertKrwToUsdWithoutFee(dto.getExchangeAmount(), dto.getExchangeRate());

    LogUtil.info("<<환전 실행>> 원화 -> 달러 환전된 금액(amount): " + amount);

    // 2. 최소 환전 금액 설정
    double minimumAmount = getMinimumAmount(dto.getCurrencyCode());
    if (amount >= minimumAmount) {
      User user = generalAccountRepository.findUserByGeneralAccountId(dto.getAccountId());
      LogUtil.info("<<환전 실행>> 모임주 userId: ", user.getUserId());

      double balance = accountService.getBalanceByAccountId(dto.getAccountId());
      LogUtil.info("<<환전실행>>모임계좌에 있던 잔액: ", balance);

      TransactionRequestDto withdrawal = new TransactionRequestDto();
      withdrawal.setTransactionBalance(Math.round(dto.getExchangeAmount()));//원화
      withdrawal.setTransactionSummary("환전 출금");
      withdrawal.setUserId(user.getUserId());
      transactionService.postAccountWithdrawal(dto.getAccountNo(), withdrawal);

      LogUtil.info("<<환전 실행>> 모임통장 계좌번호: ", dto.getAccountNo());
      LogUtil.info("<<환전 실행>> 모임통장에서 출금되는 금액: ", dto.getExchangeAmount());

      ForeignAccount foreignAccount = accountService.getForeignAccount(dto.getAccountId());
      ForeignTransactionRequestDto deposit = new ForeignTransactionRequestDto();
      deposit.setTransactionBalance(amount);//외화임
      deposit.setTransactionSummary("환전 입금");
      deposit.setUserId(user.getUserId());
      transactionService.postForeignDeposit(foreignAccount.getAccountNo(), deposit);

      LogUtil.info("<<환전 실행>> 외화 계좌 번호: ", foreignAccount.getAccountNo());
      LogUtil.info("<<환전 실행>> 외화계좌로 입금되는 금액: ", amount);

      ExchangeCurrencyDto exchangeCurrencyDto = ExchangeCurrencyDto.builder()
          .currencyCode(dto.getCurrencyCode())
          .exchangeRate(dto.getExchangeRate())//환율
          .amount(dto.getExchangeAmount())//원화
          .build();

      AccountInfoDto accountInfoDto = AccountInfoDto.builder()
          .accountId(dto.getAccountId())
          .accountNo(dto.getAccountNo())
          .amount(amount)//변경된 금액
          .balance(balance - dto.getExchangeAmount())//원래 계좌 잔액에서 amount뺀것.
          .build();

      ExchangeResponseDto responseDto = new ExchangeResponseDto();
      responseDto.setExchangeCurrencyDto(exchangeCurrencyDto);
      responseDto.setAccountInfoDto(accountInfoDto);
      responseDto.setExecuted_at(LocalDateTime.now());

      LogUtil.info("<<환전 실행>> 반환 responseDto:", responseDto);
      notificationService.notifyExchangeMessage(responseDto);

      return responseDto;
    } else {
      return null;
    }
  }

  /**
   * KRW->USD 환전된 금액 반환하는 메서드
   */
  public double convertKrwToUsdWithoutFee(double krwAmount, double exchangeRate) {
    if (exchangeRate <= 0) {
      throw new IllegalArgumentException("환율은 0보다 커야 합니다.");
    }

    // BigDecimal을 사용하여 정확한 소수점 계산
    BigDecimal krw = BigDecimal.valueOf(krwAmount);
    BigDecimal rate = BigDecimal.valueOf(exchangeRate);
    BigDecimal usdAmount = krw.divide(rate, 2, RoundingMode.HALF_UP); // 소수점 두 자리까지 반올림

    LogUtil.info("<<환전 실행>> 환전되는 금액(usdAmout) 달러 :", usdAmount.doubleValue());

    return usdAmount.doubleValue();
  }

  /**
   * USD -> KRW 환전된 금액 반환하는 메서드
   */
  public long convertUsdToKrwWithoutFee(double usdAmount, double exchangeRate) {
    if (exchangeRate <= 0) {
      return 0L;
    }

    double krwAmount = usdAmount * exchangeRate;

    return Math.round(krwAmount); // 반올림하여 정수로 반환
  }

  /**
   * 최소 환전 금액을 반환하는 메서드
   */
  private double getMinimumAmount(String currency) {
    switch (currency) {
      case "USD":
        return 100;
      case "JPY":
        return 100;
      default:
        throw new IllegalArgumentException("지원하지 않는 통화 유형입니다: " + currency);
    }
  }

  /**
   * 아래부터는 형 변환 메서드 모음
   */
  public LocalDateTime getLocalDateTime(String str) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(str, formatter);
  }

  public double getDoubleExchangeRate(String exchangeRate) {

    String exchangeRateStr = exchangeRate.replace(",", "");
    return Double.parseDouble(exchangeRateStr);
  }

  public String makeId(String currency, double rate) {
    return String.format("%s(%.2f)", currency, rate);
  }
}
