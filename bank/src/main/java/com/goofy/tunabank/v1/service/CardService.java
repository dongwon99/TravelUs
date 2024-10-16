package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Card;
import com.goofy.tunabank.v1.domain.CardProduct;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Merchant;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.domain.history.CardHistory;
import com.goofy.tunabank.v1.dto.card.CardIssueRequestDto;
import com.goofy.tunabank.v1.dto.card.CardIssueResponseDto;
import com.goofy.tunabank.v1.dto.card.CardListRequestDto;
import com.goofy.tunabank.v1.dto.card.CardNoRequestDto;
import com.goofy.tunabank.v1.dto.card.CardNoResponseDto;
import com.goofy.tunabank.v1.dto.card.CardPaymentRequestDto;
import com.goofy.tunabank.v1.dto.card.CardPaymentResponseDto;
import com.goofy.tunabank.v1.dto.card.CardRequestDto;
import com.goofy.tunabank.v1.dto.card.CardResponseDto;
import com.goofy.tunabank.v1.dto.card.CardUsageAmountRequestDto;
import com.goofy.tunabank.v1.dto.card.CardUsageAmountResponseDto;
import com.goofy.tunabank.v1.exception.account.InvalidAccountNoException;
import com.goofy.tunabank.v1.exception.card.CardExpiredException;
import com.goofy.tunabank.v1.exception.card.CardInsufficientBalanceException;
import com.goofy.tunabank.v1.exception.card.CardNotFoundException;
import com.goofy.tunabank.v1.exception.card.CardOwnershipException;
import com.goofy.tunabank.v1.exception.card.CardProductNotFoundException;
import com.goofy.tunabank.v1.exception.card.DuplicateTransactionIdException;
import com.goofy.tunabank.v1.exception.card.InvalidCvcException;
import com.goofy.tunabank.v1.exception.merchant.MerchantNotFoundException;
import com.goofy.tunabank.v1.exception.moneybox.MoneyBoxNotFoundException;
import com.goofy.tunabank.v1.mapper.CardHistoryMapper;
import com.goofy.tunabank.v1.mapper.CardMapper;
import com.goofy.tunabank.v1.mapper.HistoryMapper;
import com.goofy.tunabank.v1.repository.CardHistoryRepository;
import com.goofy.tunabank.v1.repository.CardProductRepository;
import com.goofy.tunabank.v1.repository.CardRepository;
import com.goofy.tunabank.v1.repository.MerchantRepository;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import com.goofy.tunabank.v1.repository.account.AccountRepository;
import com.goofy.tunabank.v1.util.CurrencyConverter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

  private static final String DEFAULT_ISSUER_NAME = "Tuna Bank";
  private static final SecureRandom random = new SecureRandom();
  private final PasswordEncoder passwordEncoder;

  private final UserService userService;
  private final TransactionService transactionService;

  private final CardRepository cardRepository;
  private final AccountRepository accountRepository;
  private final CardProductRepository cardProductRepository;
  private final CardHistoryRepository cardHistoryRepository;
  private final MoneyBoxRepository moneyBoxRepository;
  private final MerchantRepository merchantRepository;

  /*
   * 카드 신규 발급
   */
  public CardIssueResponseDto createNewCard(CardIssueRequestDto request) {

    // 연결 계좌 검증
    String accountNo = request.getWithdrawalAccountNo();
    Account account = accountRepository.findByAccountNo(accountNo).orElseThrow(
        () -> new InvalidAccountNoException(accountNo)
    );

    // 카드 상품 선택
    String uniqueNo = request.getCardUniqueNo();
    CardProduct cardProduct = cardProductRepository.findByUniqueNo(uniqueNo)
        .orElseThrow(() -> new CardProductNotFoundException(uniqueNo));

    //비밀번호 암호화
    String encrypted = encryptPassword(request.getPassword());

    // 카드 생성(카드 번호, cvc 번호 생성) 및 저장
    Card card = Card.createCard(account, cardProduct, generateCardNumber(), generateCvc(), encrypted);
    cardRepository.save(card);

    return CardMapper.INSTANCE.cardToCardIssueResponseDto(card, cardProduct, accountNo, DEFAULT_ISSUER_NAME);
  }


  /*
   * 카드 목록 조회
   */
  @Transactional(readOnly = true)
  public List<CardResponseDto> findAllCards(CardListRequestDto request) {

    // 유저 조회
    User user = userService.findUserByHeader();

    // 유저에 해당하는 통장/ 카드를 조인해서 조회
    List<Card> cards = cardRepository.findByUser(user);

    // DTO로 변환해서 return
    return cards.stream().map(card -> CardMapper.INSTANCE.cardToCardResponseDto(card, DEFAULT_ISSUER_NAME))
        .collect(Collectors.toList());
  }

  /*
   * 카드 단건 조회
   */
  @Transactional(readOnly = true)
  public CardResponseDto findCard(CardRequestDto request) {

    // 유저 조회
    User user = userService.findUserByHeader();

    // 카드 조회
    Card card = cardRepository.findByCardNo(request.getCardNo())
        .orElseThrow(() -> new CardNotFoundException(request.getCardNo()));

    // 유저 카드 일치 조회
    if (card.getAccount().getUser() != user) {
      throw new CardOwnershipException(request.getCardNo());
    }

    return CardMapper.INSTANCE.cardToCardResponseDto(card, DEFAULT_ISSUER_NAME);
  }

  /**
   * 계좌와 연결된 카드번호 단건 조회
   */
  @Transactional(readOnly = true)
  public CardNoResponseDto inquireCardNoByAccountNo(CardNoRequestDto request) {
    String accountNo = request.getAccountNo();
    Card card = cardRepository.findByAccountNo(accountNo).orElseThrow(() -> new CardNotFoundException(accountNo));

    return new CardNoResponseDto(card.getCardNo());
  }

  /*
   * 결제
   */
  public CardPaymentResponseDto makeCardPayment(CardPaymentRequestDto request) {

    // 카드 소유 검증
    Card card = validateCard(request.getCardNo());

    // 카드 유효 검증
    validateCardDetails(card, request.getCvc());

    // 결제 유효성 검증
    validateTransaction(request.getTransactionId());

    // 카드 잔액 확인 및 결제
    CardHistory cardHistory = processPayment(card, request);

    // 응답
    return CardHistoryMapper.INSTANCE.toCardPaymentResponseDto(cardHistory);
  }


  /*
   * 카드 번호 생성
   */
  private String generateCardNumber() {
    String bin = "400000";  // 예: Visa의 경우 일반적으로 4로 시작
    String accountNumber = String.format("%09d", random.nextInt(1000000000));
    String cardNumberWithoutCheckDigit = bin + accountNumber;
    String checkDigit = calculateLuhnCheckDigit(cardNumberWithoutCheckDigit);
    return cardNumberWithoutCheckDigit + checkDigit;
  }

  /*
   * 카드번호 가장 뒷자리, 룬 체크 알고리즘
   */
  private String calculateLuhnCheckDigit(String number) {
    int sum = 0;
    boolean alternate = false;
    for (int i = number.length() - 1; i >= 0; i--) {
      int n = Integer.parseInt(number.substring(i, i + 1));
      if (alternate) {
        n *= 2;
        if (n > 9) {
          n = (n % 10) + 1;
        }
      }
      sum += n;
      alternate = !alternate;
    }
    return Integer.toString((10 - (sum % 10)) % 10);
  }

  /*
   * CVC 생성
   */
  private String generateCvc() {
    return String.format("%03d", random.nextInt(1000));
  }

  /*
   * 카드 소유권 검증
   */
  private Card validateCard(String cardNo) {
    Card card = cardRepository.findByCardNo(cardNo).orElseThrow(() -> new CardNotFoundException(cardNo));

    User user = userService.findUserByHeader();
    User cardUser = card.getAccount().getUser();
    if (user != cardUser) {
      throw new CardOwnershipException(cardNo);
    }

    return card;
  }

  /*
   * 카드 유효 정보 검증
   */
  private void validateCardDetails(Card card, String cvc) {
    if (!card.getCvc().equals(cvc)) {
      throw new InvalidCvcException(cvc);
    }
    if (card.getExpireAt().isBefore(LocalDateTime.now())) {
      throw new CardExpiredException(card.getCardNo());
    }
  }

  /*
   * 결제 유효성 검증
   */
  private void validateTransaction(String transactionId) {
    List<CardHistory> history = cardHistoryRepository.findByTransactionId(transactionId).orElse(Collections.emptyList());
    if (!history.isEmpty()) {
      throw new DuplicateTransactionIdException(transactionId);
    }
  }

  /*
   * 결제 진행
   */
  private CardHistory processPayment(Card card, CardPaymentRequestDto request) {
    MoneyBox moneyBox = moneyBoxRepository.findMoneyBoxByAccountNoAndCurrency(card.getAccount().getAccountNo(),
            CurrencyType.valueOf(request.getCurrencyCode()))
        .orElseThrow(() -> new MoneyBoxNotFoundException(request.getCurrencyCode()));

    // 금액 확인
    Double balance = moneyBox.getBalance();
    if (balance < request.getPaymentBalance()) {
      throw new CardInsufficientBalanceException(card.getCardNo(), request.getPaymentBalance(), balance);
    }

    // 가맹점 확인
    Merchant merchant = merchantRepository.findById(request.getMerchantId())
        .orElseThrow(() -> new MerchantNotFoundException(String.valueOf(request.getMerchantId())));

    // 결제 수행 및 결과 저장
    moneyBox.payment(request.getPaymentBalance());
    return saveCardHistory(card, moneyBox, merchant, request);
  }

  /*
   * 결제 기록 저장
   */
  private CardHistory saveCardHistory(Card card, MoneyBox moneyBox, Merchant merchant, CardPaymentRequestDto request) {

    Double wonAmount = CurrencyConverter.convertToKRW(request.getPaymentBalance(), moneyBox.getCurrency().getExchangeRate())
        .doubleValue();

    Long nextId = transactionService.getNextTransactionId();

    CardHistory cardHistory = CardHistory.createCardHistory(nextId, card, moneyBox, merchant, request, wonAmount);

    cardHistoryRepository.save(cardHistory);
    return cardHistory;
  }

  /**
   * 결제 금액 조회
   */
  public CardUsageAmountResponseDto getUsageAmount(CardUsageAmountRequestDto requestDto) {

    String cardNo = requestDto.getCardNo();
    Card card = cardRepository.findByCardNo(requestDto.getCardNo())
        .orElseThrow(() -> new CardNotFoundException(cardNo));

    double krwAmount = cardHistoryRepository.getTotalAmountByCurrency(cardNo, CurrencyType.KRW);
    CurrencyType foreignCurrency=card.getAccount().getMoneyBoxes().get(1).getCurrency().getCurrencyCode();
    double foreignAmount= cardHistoryRepository.getTotalAmountByCurrency(cardNo,foreignCurrency);

    return new CardUsageAmountResponseDto(krwAmount,foreignAmount);
  }


  // 비밀번호 암호화
  public String encryptPassword(String plainPassword) {
    return passwordEncoder.encode(plainPassword);
  }

  // 비밀번호 검증
  public boolean verifyPassword(String plainPassword, String encryptedPassword) {
    return passwordEncoder.matches(plainPassword, encryptedPassword);
  }

}
