package com.ssafy.soltravel.v2.service.transaction;

import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.domain.Enum.ExchangeType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionHistoryListRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.HistoryResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransactionResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.service.NotificationService;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.group.GroupService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

  private final Map<String, String> apiKeys;

  private final UserRepository userRepository;

  private final SecurityUtil securityUtil;
  private final WebClientUtil webClientUtil;
  private final WebClient webClient;

  private final ModelMapper modelMapper;

  private final String BASE_URL = "/transaction/";
  private final NotificationService notificationService;
  private final AccountService accountService;
  private final GroupService groupService;

  /**
   * 입금
   */
  @Transactional
  public ResponseEntity<TransactionResponseDto> postAccountDeposit(
      TransactionRequestDto requestDto, long userId) {

    //정산입금
    if (requestDto.getTransactionType().equals(TransactionType.SD)) {
      return processTransaction("deposit", requestDto, true, userId);
    }
    return processTransaction("deposit", requestDto, false, userId);
  }

  /**
   * 출금
   */
  @Transactional
  public ResponseEntity<TransactionResponseDto> postAccountWithdrawal(
      TransactionRequestDto requestDto, long userId) {

    //정산출금
    if (requestDto.getTransactionType().equals(TransactionType.SW)) {

      return processTransaction("withdrawal", requestDto, true, userId);
    }
    return processTransaction("withdrawal", requestDto, false, userId);
  }

  /**
   * 일반 이체
   */
  @Transactional
  public ResponseEntity<List<TransferHistoryResponseDto>> postGeneralTransfer(
      TransferRequestDto requestDto) {

    return processTransfer("general", requestDto);
  }

  /**
   * 머니 박스 이체
   */
  @Transactional
  public ResponseEntity<List<TransferHistoryResponseDto>> postMoneyBoxTransfer(
      MoneyBoxTransferRequestDto requestDto, boolean isAuto, long userId) {

    return processMoneyBoxTransfer("moneybox", requestDto, isAuto, userId);
  }

  /**
   * 입출금 공통 요청 처리 메서드
   */
  @Transactional
  public ResponseEntity<TransactionResponseDto> processTransaction(String apiName,
      TransactionRequestDto requestDto, boolean isSettlement, long userId) {

    String API_URL = BASE_URL;

    if (isSettlement) {//자동정산이라면

      API_URL += ("/settlement/" + apiName);
    } else {

      API_URL += apiName;
      userId = securityUtil.getCurrentUserId();
    }

    long finalUserId = userId;

    User user = userRepository.findByUserId(finalUserId)
        .orElseThrow(() -> new UserNotFoundException(finalUserId));

    Header header = Header.builder()
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey()).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("accountNo", requestDto.getAccountNo());
    body.put("accountPassword", requestDto.getAccountPassword());
    body.put("currencyCode", requestDto.getCurrencyCode());
    body.put("transactionType", requestDto.getTransactionType());
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("transactionSummary", requestDto.getTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    Object recObject = response.getBody().get("REC");

    TransactionResponseDto transactionResponseDto = modelMapper.map(recObject, TransactionResponseDto.class);

    notificationService.sendDepositNotification(userId, requestDto);

    return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDto);
  }

  /**
   * 이체 요청 처리 메서드
   */
  @Transactional
  public ResponseEntity<List<TransferHistoryResponseDto>> processTransfer(String apiName,
      TransferRequestDto requestDto) {

    User user = securityUtil.getUserByToken();

    String API_URL = BASE_URL + "transfer/" + apiName;

    Header header = Header.builder()
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey()).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("transferType", requestDto.getTransferType());
    body.put("withdrawalAccountNo", requestDto.getWithdrawalAccountNo());
    body.put("accountPassword", requestDto.getAccountPassword());
    body.put("depositAccountNo", requestDto.getDepositAccountNo());
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("withdrawalTransactionSummary", requestDto.getWithdrawalTransactionSummary());
    body.put("depositTransactionSummary", requestDto.getDepositTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    List<Object> recObject = (List<Object>) response.getBody().get("REC");

    List<TransferHistoryResponseDto> responseDto = recObject.stream()
        .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
        .collect(Collectors.toList());

    String depositAccountNo = requestDto.getDepositAccountNo();
    AccountDto accountDto = accountService.getByAccountNo(depositAccountNo);
    List<TransferHistoryResponseDto> exchangeResponseDto = null;
    if (accountDto.getAccountType() == AccountType.G) {

      GroupDto group = groupService.getGroupByAccountNo(depositAccountNo);
      if (group.getExchangeType() == ExchangeType.NOW) {

        Map<String, Object> exchangeBody = new HashMap<>();

        exchangeBody.put("Header", header);
        exchangeBody.put("transferType", TransferType.M);
        exchangeBody.put("accountNo", depositAccountNo);
        exchangeBody.put("accountPassword", accountDto.getAccountPassword());
        exchangeBody.put("sourceCurrencyCode", accountDto.getMoneyBoxDtos().get(0).getCurrencyCode());
        exchangeBody.put("targetCurrencyCode", accountDto.getMoneyBoxDtos().get(1).getCurrencyCode());
        exchangeBody.put("transactionBalance", accountDto.getMoneyBoxDtos().get(0).getBalance());

        try {
          ResponseEntity<Map<String, Object>> exchangeResponse = webClient.post()
              .uri(BASE_URL + "transfer/moneybox/auto")
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(exchangeBody)
              .retrieve()
              .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
              })
              .block();

          List<Object> exchangeRecObject = (List<Object>) exchangeResponse.getBody().get("REC");

          exchangeResponseDto = exchangeRecObject.stream()
              .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
              .collect(Collectors.toList());

          responseDto.addAll(exchangeResponseDto);
        } catch (Exception e) {
          LogUtil.info("환전 실패(잔액 부족): ", e.getMessage());
        }
      }
    }

    notificationService.sendTransferNotification(user, requestDto);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }


  /**
   * 머니박스 이체 요청 처리 메서드
   */
  @Transactional
  public ResponseEntity<List<TransferHistoryResponseDto>> processMoneyBoxTransfer(
      String apiName,
      MoneyBoxTransferRequestDto requestDto,
      boolean isAuto,
      long userId
  ) {

    String API_URL = BASE_URL + "transfer/" + apiName;
    if (isAuto) {//자동환전이라면

      API_URL += "/auto";
    } else {
      userId = securityUtil.getCurrentUserId();
    }
    long finalUserId = userId;

    User user = userRepository.findByUserId(finalUserId)
        .orElseThrow(() -> new UserNotFoundException(finalUserId));

    Header header = Header.builder()
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey()).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("transferType", requestDto.getTransferType());
    body.put("accountNo", requestDto.getAccountNo());
    body.put("accountPassword", requestDto.getAccountPassword());
    body.put("sourceCurrencyCode", requestDto.getSourceCurrencyCode());
    body.put("targetCurrencyCode", requestDto.getTargetCurrencyCode());
    body.put("transactionBalance", requestDto.getTransactionBalance());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    List<Object> recObject = (List<Object>) response.getBody().get("REC");

    List<TransferHistoryResponseDto> responseDto = recObject.stream()
        .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
        .collect(Collectors.toList());

    if (!isAuto) {
      notificationService.sendExchangeNotification(user, requestDto);
    }

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  /**
   * 거래 내역 목록 조회
   */
  @Transactional
  public ResponseEntity<Page<HistoryResponseDto>> getHistoryList(
      TransactionHistoryListRequestDto requestDto) {

    User user = securityUtil.getUserByToken();

    String API_URL = BASE_URL + "history";

    Header header = Header.builder()
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey()).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    TransactionType transactionType = requestDto.getTransactionType();
    if (transactionType != null) {
      body.put("transactionType", transactionType);
    }
    body.put("accountNo", requestDto.getAccountNo());
    body.put("currencyCode", requestDto.getCurrencyCode());
    body.put("startDate", requestDto.getStartDate());
    body.put("endDate", requestDto.getEndDate());
    body.put("orderByType", requestDto.getOrderByType());
    body.put("page", requestDto.getPage());
    body.put("size", requestDto.getSize());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    Map<String, Object> recMap = (Map<String, Object>) response.getBody().get("REC");
    List<Map<String, Object>> content = (List<Map<String, Object>>) recMap.get("content");
    List<HistoryResponseDto> responseDto = content.stream()
        .map(value -> {
          HistoryResponseDto dto = modelMapper.map(value, HistoryResponseDto.class);

          if (value.get("transactionUniqueNo") instanceof Integer) {
            dto.setTransactionUniqueNo(((Integer) value.get("transactionUniqueNo")).longValue());
          }
          return dto;
        })
        .collect(Collectors.toList());

    // pageable 정보 추출
    Map<String, Object> pageable = (Map<String, Object>) recMap.get("pageable");
    int page = (int) pageable.get("pageNumber");
    int size = (int) pageable.get("pageSize");

    Object totalElementsObj = recMap.get("totalElements");
    long totalElements = 0L; // 기본값을 0으로 설정

    if (totalElementsObj != null) {
      if (totalElementsObj instanceof Integer) {
        totalElements = ((Integer) totalElementsObj).longValue();
      } else if (totalElementsObj instanceof Long) {
        totalElements = (Long) totalElementsObj;
      }
    }

    Pageable pageableObj = PageRequest.of(page, size);
    Page<HistoryResponseDto> pagedResponseDto = new PageImpl<>(responseDto, pageableObj,
        totalElements);

    return ResponseEntity.status(HttpStatus.OK).body(pagedResponseDto);
  }

  /**
   * 거래 내역 단건 조회
   */
  @Transactional
  public ResponseEntity<HistoryResponseDto> getHistory(
      TransactionHistoryRequestDto requestDto) {

    User user = securityUtil.getUserByToken();

    String API_URL = BASE_URL + "history/detail";

    Header header = Header.builder()
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey()).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("transactionHistoryId", requestDto.getTransactionHistoryId());
    body.put("transactionType", requestDto.getTransactionType());
    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);
    Object recObject = response.getBody().get("REC");

    HistoryResponseDto responseDto = modelMapper.map(recObject,
        HistoryResponseDto.class);

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}
