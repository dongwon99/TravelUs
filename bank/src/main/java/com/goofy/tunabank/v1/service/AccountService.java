package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Bank;
import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.TransferType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.account.AccountDto;
import com.goofy.tunabank.v1.dto.account.request.AddMoneyBoxRequestDto;
import com.goofy.tunabank.v1.dto.account.request.BalanceRequestDto;
import com.goofy.tunabank.v1.dto.account.request.CreateGeneralAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.DeleteAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.InquireAccountListRequestDto;
import com.goofy.tunabank.v1.dto.account.request.InquireAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.PasswordValidateRequestDto;
import com.goofy.tunabank.v1.dto.account.response.BalanceResponseDto;
import com.goofy.tunabank.v1.dto.account.response.DeleteAccountResponseDto;
import com.goofy.tunabank.v1.dto.account.response.PasswordValidateResponseDto;
import com.goofy.tunabank.v1.dto.moneyBox.MoneyBoxDto;
import com.goofy.tunabank.v1.dto.moneyBox.request.DeleteMoneyBoxRequestDto;
import com.goofy.tunabank.v1.dto.moneyBox.response.DeleteMoneyBoxResponseDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferMBRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.exception.account.DuplicateCurrencyException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountNoException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountPasswordException;
import com.goofy.tunabank.v1.exception.account.InvalidBankIdException;
import com.goofy.tunabank.v1.exception.account.InvalidGroupAccountIdException;
import com.goofy.tunabank.v1.exception.account.RefundAccountRequiredException;
import com.goofy.tunabank.v1.exception.account.UserAccountsNotFoundException;
import com.goofy.tunabank.v1.exception.moneybox.InvalidDeleteCurrencyException;
import com.goofy.tunabank.v1.exception.moneybox.MoneyBoxNotFoundException;
import com.goofy.tunabank.v1.mapper.AccountMapper;
import com.goofy.tunabank.v1.mapper.MoneyBoxMapper;
import com.goofy.tunabank.v1.repository.BankRepository;
import com.goofy.tunabank.v1.repository.CurrencyRepository;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import com.goofy.tunabank.v1.repository.account.AccountRepository;
import com.goofy.tunabank.v1.util.LogUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final BankRepository bankRepository;
    private final MoneyBoxRepository moneyBoxRepository;

    private final UserService userService;
    private final TransactionService transactionService;

    private final AccountMapper accountMapper;
    private final MoneyBoxMapper moneyBoxMapper;

    private final PasswordEncoder passwordEncoder;

    // ==== 계좌 생성 관련 메서드 ====
    public AccountDto postNewAccount(CreateGeneralAccountRequestDto requestDto) {

        // 유저 정보 생성
        User user = userService.findUserByHeader();

        // 통화 및 은행 정보 조회
        Currency currency = currencyRepository.findByCurrencyCode(CurrencyType.KRW);
        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new InvalidBankIdException(requestDto.getBankId()));

        // 비밀번호 암호화
        String plainPassword = requestDto.getAccountPassword();
        requestDto.setAccountPassword(passwordEncoder.encode(plainPassword));

        // 계좌 생성
        Account account = Account.createAccount(requestDto, bank, user);

        // 계좌 최초 생성 시 KRW 머니박스 기본 생성
        MoneyBox moneyBox = MoneyBox.createMoneyBox(account, currency);

        if (account.getAccountType() == AccountType.G) {
            moneyBox.setBalance(0.0);
        }

        account.addMoneyBox(moneyBox);

        accountRepository.save(account);

        return accountMapper.toDto(account);
    }

    // ==== 유저 계좌 전체 조회 ====
    public List<AccountDto> inqureAccountList(InquireAccountListRequestDto requestDto) {

        User user = userService.findUserByHeader();

        List<Account> accountList = accountRepository.findAllAccountsByUserId(user.getUserId())
            .orElseThrow(() -> new UserAccountsNotFoundException(user.getUserId()));

        String searchType = requestDto.getSearchType();

        List<AccountDto> accountDtoList = accountList.stream()
            .filter(account -> searchType.equals("A") || account.getAccountType().name().equals(searchType))
            .map(accountMapper::toDto)
            .collect(Collectors.toList());

        return accountDtoList;
    }

    // ==== 계좌 조회 ====
    public AccountDto inquireAccount(InquireAccountRequestDto requestDto) {

        User user = userService.findUserByHeader();

        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        // DTO 변환
        AccountDto accountDto = accountMapper.toDto(account);
        accountDto.setCredentialId(account.getUser().getCredentialId());

        return accountDto;
    }


    // ==== 계좌 삭제 ====
    public DeleteAccountResponseDto deleteAccount(DeleteAccountRequestDto requestDto) {

        // 계좌 조회 및 비밀번호 확인
        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        if (!passwordEncoder.matches(requestDto.getAccountPassword(), account.getAccountPassword())) {
            throw new InvalidAccountPasswordException(requestDto.getAccountPassword());
        }

        // 계좌 잔액 여부 판단
        DeleteAccountResponseDto dto = validateAndRefundAccount(account, requestDto);

        LogUtil.info("dto", dto);

        // 머니박스 및 계좌 상태 변경
        account.getMoneyBoxes().forEach(MoneyBox::closeMoneyBox);
        account.closeAccount();

        return dto;
    }

    /*
     *  머니박스 관련 메서드
     */

    // ==== 머니 박스 생성 ====
    public List<MoneyBoxDto> addAccountMoneyBox(AddMoneyBoxRequestDto requestDto) {

        // 유저 정보 생성
        User user = userService.findUserByHeader();

        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        if (!passwordEncoder.matches(requestDto.getAccountPassword(), account.getAccountPassword())) {
            throw new InvalidAccountPasswordException(requestDto.getAccountPassword());
        }

        if (!account.getAccountType().equals(AccountType.G)) {
            throw new InvalidGroupAccountIdException();
        }

        boolean isExistCurrencyCode = account.getMoneyBoxes().stream()
            .anyMatch(v -> v.getCurrency().getCurrencyCode().equals(requestDto.getCurrencyCode()));

        if (isExistCurrencyCode) {
            throw new DuplicateCurrencyException(requestDto.getCurrencyCode());
        }

        Currency currency = currencyRepository.findByCurrencyCode(requestDto.getCurrencyCode());

        MoneyBox moneyBox = MoneyBox.createMoneyBox(account, currency);

        moneyBox.setBalance(0.0);

        account.getMoneyBoxes().add(moneyBox);

        accountRepository.save(account);

        return moneyBoxMapper.toDtoList(account.getMoneyBoxes());
    }

    // 머니박스 해지
    @Transactional
    public DeleteMoneyBoxResponseDto deleteMoneyBox(DeleteMoneyBoxRequestDto requestDto) {

        if (requestDto.getCurrencyCode().toString().equals("KRW")) {
            throw new InvalidDeleteCurrencyException();
        }

        User user = userService.findUserByHeader();

        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        // 머니박스 존재 여부
        MoneyBox moneyBox = account.getMoneyBoxes().stream()
            .filter(box -> box.getCurrency().getCurrencyCode().equals(requestDto.getCurrencyCode()))
            .findFirst()
            .orElseThrow(() -> new MoneyBoxNotFoundException("해당 통화의 MoneyBox가 존재하지 않습니다."));

        // 외화 머니박스 잔액 -> KRW 환전
        MoneyBox krwMoneyBox = account.getMoneyBoxes().get(0);

        double beforeRefundAmount = krwMoneyBox.getBalance();

        if (moneyBox.getBalance() > 0) {
            TransferMBRequestDto dto = TransferMBRequestDto.from(
                account,
                moneyBox,
                requestDto.getHeader()
            );

            dto.setAccountPassword(requestDto.getAccountPassword());
            transactionService.processMoneyBoxTransfer(dto, false);
        }

        moneyBox.closeMoneyBox();

        double afterRefundAmount = krwMoneyBox.getBalance();
        double calcAmount = afterRefundAmount - beforeRefundAmount;

        return new DeleteMoneyBoxResponseDto("CLOSED", account.getAccountNo(), calcAmount);
    }


    public BalanceResponseDto getBalanceByAccountNo(BalanceRequestDto requestDto) {

        MoneyBox moneyBox = transactionService.findMoneyBoxByAccountAndCurrencyCode(requestDto.getAccountNo(),
            requestDto.getCurrencyCode());
        return new BalanceResponseDto(moneyBox.getBalance());
    }


    // 머니박스 잔액 검증 & 환불 함수
    private DeleteAccountResponseDto validateAndRefundAccount(Account account, DeleteAccountRequestDto requestDto) {

        Account refundAccount = accountRepository.findByAccountNo(requestDto.getRefundAccountNo())
            .orElseThrow(() -> new RefundAccountRequiredException(requestDto.getRefundAccountNo()));

        List<MoneyBox> moneyBoxes = account.getMoneyBoxes();

        double refundAmount = 0.0;

        // 외화 머니박스에 돈이 있으면 원화 머니박스로 자동 환전
        if (moneyBoxes.size() > 1 && moneyBoxes.get(1).getBalance() > 0) {
            refundAmount = moneyBoxes.get(1).getBalance();
            TransferMBRequestDto dto = TransferMBRequestDto.from(account, moneyBoxes.get(1), requestDto.getHeader());

            transactionService.processMoneyBoxTransfer(dto, false);
        }

        if (moneyBoxes.get(0).getBalance() > 0) {
            refundAmount = moneyBoxes.get(0).getBalance();
            TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                .header(requestDto.getHeader())
                .transferType(TransferType.G)
                .withdrawalAccountNo(account.getAccountNo())
                .accountPassword(account.getAccountPassword())
                .depositAccountNo(refundAccount.getAccountNo())
                .transactionBalance(moneyBoxes.get(0).getBalance()).build();

            transactionService.processGeneralTransfer(transferRequestDto);
        }

        DeleteAccountResponseDto dto = new DeleteAccountResponseDto("CLOSED", refundAccount.getAccountNo(), refundAmount);

        return dto;
    }

    public PasswordValidateResponseDto validatePassword(PasswordValidateRequestDto requestDto) {

        PasswordValidateResponseDto responseDto = new PasswordValidateResponseDto();
        responseDto.setResult(
            transactionService.validatePassword(requestDto.getAccountPassword(), requestDto.getAccountNo(), false) ? "success"
                : "fail");
        return responseDto;
    }

}
