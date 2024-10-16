package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.common.RecWrapper;
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
import com.goofy.tunabank.v1.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 일반 계좌 생성 ( 개인 or 모임 )
    @PostMapping("/postAccount")
    public ResponseEntity<RecWrapper<AccountDto>> createNewAccount(
        @RequestBody CreateGeneralAccountRequestDto requestDto
    ) {

        AccountDto responseDto = accountService.postNewAccount(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(responseDto));

    }

    // 유저 계좌 전체 조회
    @PostMapping("/inquireAccountList")
    public ResponseEntity<RecWrapper<List<AccountDto>>> createNewAccount(
        @RequestBody InquireAccountListRequestDto requestDto) {

        List<AccountDto> accountDtoList = accountService.inqureAccountList(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(accountDtoList));
    }

    // 계좌 조회
    @PostMapping("/inquireAccount")
    public ResponseEntity<RecWrapper<AccountDto>> inquireAccount(
        @RequestBody InquireAccountRequestDto requestDto
    ) {
        AccountDto responseDto = accountService.inquireAccount(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(responseDto));
    }

    // 계좌 삭제
    @PostMapping("/deleteAccount")
    public ResponseEntity<RecWrapper<DeleteAccountResponseDto>> deleteAccount(
        @RequestBody DeleteAccountRequestDto requestDto
    ) {
        DeleteAccountResponseDto responseDto = accountService.deleteAccount(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(responseDto));
    }

    /*
     *  머니박스 추가
     */
    // 모임 통장 머니박스 추가
    @PostMapping("/addMoneyBox")
    public ResponseEntity<RecWrapper<List<MoneyBoxDto>>> addAccountMoneyBox(
        @RequestBody AddMoneyBoxRequestDto requestDto
    ) {

        List<MoneyBoxDto> responseDto = accountService.addAccountMoneyBox(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(responseDto));
    }

    // 머니박스 삭제
    @PostMapping("/deleteMoneyBox")
    public ResponseEntity<RecWrapper<DeleteMoneyBoxResponseDto>> deleteMoneyBox(
        @RequestBody DeleteMoneyBoxRequestDto requestDto
    ) {
        DeleteMoneyBoxResponseDto responseDto = accountService.deleteMoneyBox(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(responseDto));
    }

    /**
     * 서비스용 잔액조회 메서드
     */
    @PostMapping("/balance")
    public ResponseEntity<RecWrapper<BalanceResponseDto>> getBalanceByAccountNo(
        @RequestBody BalanceRequestDto requestDto) {

        BalanceResponseDto responseDto = accountService.getBalanceByAccountNo(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(responseDto));
    }

  /**
   * 비밀번호 검증
   */
  @PostMapping("/validate-password")
  public ResponseEntity<RecWrapper<PasswordValidateResponseDto>> validatePassword(
      @RequestBody PasswordValidateRequestDto requestDto) {

    return ResponseEntity.status(HttpStatus.OK).body(new RecWrapper<>(accountService.validatePassword(requestDto)));
  }
}
