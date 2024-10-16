package com.ssafy.soltravel.v2.service.exchange;


import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.ExchangeType;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.domain.TargetRate;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.PasswordValidateRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeModeUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.TargetRateUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.targetAccountDto;
import com.ssafy.soltravel.v2.dto.moneyBox.response.BalanceResponseDto;
import com.ssafy.soltravel.v2.dto.targetRate.TargetRateDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.group.GroupNotFoundException;
import com.ssafy.soltravel.v2.exception.targetrate.TargetRateNotFoundException;
import com.ssafy.soltravel.v2.mapper.ExchangeRateMapper;
import com.ssafy.soltravel.v2.mapper.TargetRateMapper;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.TargetRateRepository;
import com.ssafy.soltravel.v2.service.NotificationService;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeService {

  private final String BASE_URL = "/exchange/";
  private final Map<String, String> apiKeys;
  private final WebClientUtil webClientUtil;
  private final ModelMapper modelMapper;
  private final ExchangeRateMapper exchangeRateMapper;

  private final CacheManager cacheManager;
  private final RedisTemplate<String, String> redisTemplate;
  private final TransactionService transactionService;
  private final NotificationService notificationService;
  private final AccountService accountService;
  private final GroupRepository groupRepository;
  private final ExchangeRateForecastRepository exchangeRateForecastRepository;
  private final TargetRateRepository targetRateRepository;
  private final TargetRateMapper targetRateMapper;

  private List<String> Currencies = List.of("USD", "JPY", "EUR", "TWD");

  /**
   * 현재 환율 전체 조회
   */
  @Transactional(readOnly = true)
  public List<ExchangeRateResponseDto> getExchangeRateAll() {

    List<ExchangeRateResponseDto> rateEntity = new ArrayList<>();
    for (String currency : Currencies) {

      rateEntity.add(getExchangeRate(currency));
    }
    return rateEntity;
  }


  /**
   * 현재 환율 단건 조회
   */
  @Transactional(readOnly = true)
  public ExchangeRateResponseDto getExchangeRate(String currency) {

    ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateMapper.toExchangeRateResponseDto(
        getExchangeRateFromCache(currency));
    return exchangeRateResponseDto;
  }

  /**
   * 목표 환율 설정
   */
  @Transactional
  public ResponseDto setPreferenceRate(ExchangeRateRegisterRequestDto dto, boolean isUpdate, long targetId) {

    long groupId = dto.getGroupId();
    TravelGroup group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

    //TODO:비밀번호 검증
    accountService.validatePassword(new PasswordValidateRequestDto(group.getGroupAccountNo(), dto.getAccountPassword()));

    CurrencyType currencyCode = dto.getCurrencyCode();
    double targetRate = BigDecimal.valueOf(dto.getTargetRate()).setScale(2, RoundingMode.HALF_UP).doubleValue();

    if (!isUpdate) {

      Double amount = dto.getTransactionBalance() != null ? dto.getTransactionBalance() : -1.0;
      TargetRate target = targetRateRepository.save(TargetRate.createTargetRate(amount, targetRate, group));
      targetId = target.getId();
    }

    String key = currencyCode + ":targets";
    String value = String.valueOf(targetId);

    redisTemplate.opsForZSet().add(key, value, targetRate);

    if (dto.getDueDate() != null) {
      LocalDate dueDate = dto.getDueDate();
      LocalDateTime endOfDay = dueDate.atTime(LocalTime.of(23, 59, 59));
      LocalDateTime now = LocalDateTime.now();

      if (endOfDay.isAfter(now)) {
        Duration duration = Duration.between(now, endOfDay);
        long ttlInSeconds = duration.getSeconds();
        redisTemplate.expire(key, ttlInSeconds, TimeUnit.SECONDS);
      } else {
        redisTemplate.expire(key, 0, TimeUnit.SECONDS);
      }
    }
    return new ResponseDto();
  }

  /**
   * 희망환율 수정
   */
  @Transactional
  public ResponseDto updateTargetRate(TargetRateUpdateRequestDto requestDto) {

    long groupId = requestDto.getGroupId();
    TargetRate targetRate = targetRateRepository.findByGroupId(groupId)
        .orElseThrow(() -> new TargetRateNotFoundException(groupId));
    removePreferenceRateFromRedis(requestDto.getCurrencyCode(), targetRate.getId());
    targetRate.setRate(BigDecimal.valueOf(requestDto.getTargetRate()).setScale(2, RoundingMode.HALF_UP).doubleValue());
    targetRate.setAmount(requestDto.getTransactionBalance());
    targetRateRepository.save(targetRate);

    setPreferenceRate(new ExchangeRateRegisterRequestDto(groupId, requestDto.getAccountPassword(),
        getCurrencyType(requestDto.getCurrencyCode()),
        requestDto.getTransactionBalance(), requestDto.getTargetRate(), requestDto.getDueDate()), true, targetRate.getId());

    return new ResponseDto();
  }

  /**
   * 희망환율 조회 메서드
   */
  public TargetRateDto getTargetRate(long groupId) {

    TargetRate response = targetRateRepository.findByGroupId(groupId)
        .orElseThrow(() -> new TargetRateNotFoundException(groupId));

    return targetRateMapper.toTargetRateDto(response);
  }

  /**
   * 희망환율 삭제
   */
  @Transactional
  public ResponseDto deleteTargetRate(Long groupId) {

    TravelGroup group = groupRepository.findById(groupId)
        .orElseThrow(() -> new GroupNotFoundException(groupId));

    AccountDto account = accountService.getByAccountNo(group.getGroupAccountNo());

    removePreferenceRateFromRedis(account.getMoneyBoxDtos().get(1).getCurrencyCode().toString(), group.getTargetRate().getId());
    targetRateRepository.deleteByGroupId(groupId);
    return new ResponseDto();
  }

  /**
   * 환전 모드 변경 메서드
   */
  public ResponseDto updateExchangeMode(ExchangeModeUpdateRequestDto requestDto) {

    TravelGroup group = groupRepository.findById(requestDto.getGroupId())
        .orElseThrow(() -> new GroupNotFoundException(requestDto.getGroupId()));

    AccountDto account = accountService.getByAccountNo(group.getGroupAccountNo());

    //원래 환전모드가 자동환전이었다면 대기열에서 삭제
    if (group.getExchangeType() == ExchangeType.AUTO) {
      removePreferenceRateFromRedis(account.getMoneyBoxDtos().get(1).getCurrencyCode().toString(), group.getTargetRate().getId());
    }

    group.setExchangeType(requestDto.getExchangeType());
    group.setUpdatedAt(LocalDateTime.now());
    groupRepository.save(group);

    return new ResponseDto();
  }

  /**
   * 자동 환전
   */
  @Transactional
  public void processCurrencyConversions(String currencyCode, Double exchangeRate) {

    Set<targetAccountDto> list = getAccountsForRateHigherThan(currencyCode, exchangeRate);
    for (targetAccountDto dto : list) {

      MoneyBoxTransferRequestDto requestDto = MoneyBoxTransferRequestDto.create(TransferType.M, dto.getAccountNo(), null,
          CurrencyType.KRW, getCurrencyType(currencyCode), dto.getAmount());

      try {
        List<TransferHistoryResponseDto> transferHistoryResponseDtos = transactionService.postMoneyBoxTransfer(requestDto,
            true,
            dto.getUserId()).getBody();

        removePreferenceRateFromRedis(currencyCode, dto.getTargetId());

        LogUtil.info("자동환전 성공. 환전 신청 원화: %s, 적용 환율: %s, 환전된 금액: %s ",
            transferHistoryResponseDtos.get(0).getTransactionAmount(),
            transferHistoryResponseDtos.get(1).getTransactionSummary(),
            transferHistoryResponseDtos.get(1).getTransactionAmount());

        TargetRate target = targetRateRepository.findById(dto.getTargetId())
            .orElseThrow(() -> new TargetRateNotFoundException(dto.getTargetId()));
        target.setStatus(SettlementStatus.COMPLETED);

        notificationService.sendAutoExchangeNotification(dto, requestDto);

      } catch (WebClientResponseException e) {
        //잔액부족시
        if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
          LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: INSUFFICIENT_BALANCE", dto.getAccountNo(),
              dto.getUserId());

        } else {
          LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: {}", dto.getAccountNo(), dto.getUserId(), e.getMessage());
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      } catch (Exception e) {
        LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: {}", dto.getAccountNo(), dto.getUserId(), e.getMessage());
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      }
    }
  }

  /**
   * 실시간 환율 <= 목표 환율인 계좌 목록 조회
   */
  @Transactional(readOnly = true)
  public Set<targetAccountDto> getAccountsForRateHigherThan(String currencyCode, double realTimeRate) {
    boolean isAll = false;
    String key = currencyCode + ":targets";

    // 실시간 환율보다 높은 모든 계좌 ID와 금액을 조회 (ZSET에서 score가 실시간 환율보다 큰 요소들을 가져옴)
    Set<String> results = redisTemplate.opsForZSet().rangeByScore(key, realTimeRate, Double.MAX_VALUE);

    // 분리하여 AccountWithAmount 객체로 변환
    Set<targetAccountDto> accounts = new HashSet<>();
    if (results != null) {
      for (String targetId : results) {

        Long id = Long.parseLong(targetId);
        TargetRate target = targetRateRepository.findById(id).orElseThrow(() -> new TargetRateNotFoundException(id));

        String accountNo = target.getGroup().getGroupAccountNo();
        double amount = target.getAmount();
        long userId = groupRepository.findMasterUserIdByAccountNo(accountNo);

        if (amount == -1) {

          amount = getKRWBalanceByAccountNo(accountNo);
          isAll = true;
        }

        double targetRate = target.getRate();
        accounts.add(new targetAccountDto(id, accountNo, userId, amount, targetRate, isAll));
      }
    }
    return accounts;
  }

  /**
   * Redis에서 값 삭제하는 메서드
   */
  @Transactional
  public void removePreferenceRateFromRedis(String currencyCode, long targetId) {
    String key = currencyCode + ":targets";
    String value = String.valueOf(targetId);

    redisTemplate.opsForZSet().remove(key, value);
  }


  /**
   * String의 currencyCode를 CurreucyType으로 변환
   */
  public CurrencyType getCurrencyType(String currencyCode) {

    return switch (currencyCode) {
      case "USD" -> CurrencyType.USD;
      case "JPY" -> CurrencyType.JPY;
      case "EUR" -> CurrencyType.EUR;
      case "TWD" -> CurrencyType.TWD;
      default -> CurrencyType.KRW;
    };
  }

  /**
   * ----------------------환율 캐시관련 메서드----------------------
   */

  /**
   * 환율 캐시 업데이트
   */
  @CachePut(value = "exchangeRates", key = "#dto.currencyCode")
  public void updateExchangeRateCache(ExchangeRateCacheDto dto) {

    Cache cache = cacheManager.getCache("exchangeRates");
    if (cache != null) {
      cache.put(dto.getCurrencyCode(), dto);
    }
  }

  /**
   * cache에서 환율 조회
   */
  @Cacheable(value = "exchangeRates", key = "#currencyCode")
  public ExchangeRateCacheDto getExchangeRateFromCache(String currencyCode) {

    Cache cache = cacheManager.getCache("exchangeRates");
    if (cache != null) {
      ExchangeRateCacheDto cachedDto = cache.get(currencyCode, ExchangeRateCacheDto.class);
      if (cachedDto != null) {
        return cachedDto;
      }
    }

    // 외부 API에서 환율 정보 가져오기
    Header header = Header.builder().apiKey(apiKeys.get("API_KEY")).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(BASE_URL + currencyCode, body, Map.class);
    Object recObject = response.getBody().get("REC");

    ExchangeRateCacheDto rateDto = modelMapper.map(recObject, ExchangeRateCacheDto.class);
    if (rateDto.getCurrencyCode().equals(currencyCode)) {
      return rateDto; // 환율 반환
    }
    return null;
  }

  /**
   * 계좌 잔액 조회 메서드
   */
  public double getKRWBalanceByAccountNo(String accountNo) {
    Header header = Header.builder().apiKey(apiKeys.get("API_KEY")).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("currencyCode", "KRW");

    ResponseEntity<Map<String, Object>> response = webClientUtil.request("/accounts/balance", body, Map.class);
    Object recObject = response.getBody().get("REC");

    BalanceResponseDto responseDto = modelMapper.map(recObject, BalanceResponseDto.class);

    LogUtil.info("계좌 잔액 responseDto.balance", responseDto.getBalance());
    return Double.parseDouble(responseDto.getBalance());
  }
}
