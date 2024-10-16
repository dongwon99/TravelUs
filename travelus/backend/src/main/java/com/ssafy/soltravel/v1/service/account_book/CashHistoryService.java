package com.ssafy.soltravel.v1.service.account_book;

import com.ssafy.soltravel.v1.domain.CashHistory;
import com.ssafy.soltravel.v1.domain.ForeignAccount;
import com.ssafy.soltravel.v1.exception.LackOfBalanceException;
import com.ssafy.soltravel.v1.repository.CashHistoryRepository;
import com.ssafy.soltravel.v1.util.DateUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CashHistoryService {

  private final CashHistoryRepository cashHistoryRepository;

  public void saveCashHistory(CashHistory cashHistory) {
    cashHistoryRepository.save(cashHistory);
  }


  /*
  * 현금 출금
  */
  public Double getCashFromAccount(ForeignAccount foreignAccount, Double amount) {

    // 마지막 현금 잔액 확인
    CashHistory lastHistory = cashHistoryRepository.findLastHistory().orElse(null);
    Double lastBalance = (lastHistory == null) ? 0 : lastHistory.getBalance();

    // 현금 잔액 update
    Double newBalance = lastBalance + amount;

    // 변동된 금액 적용한 history 엔티티 생성
    CashHistory newHistory = CashHistory.createGetCashHistory(
        foreignAccount, amount, newBalance, ""
    );

    // 저장
    cashHistoryRepository.save(newHistory);
    return newBalance;
  }

  /*
  * 현금 사용
  */
  public Double payCash(
      ForeignAccount foreignAccount, Double amount, String store, LocalDateTime transactionAt
  ) throws LackOfBalanceException {

/*
    // 마지막 현금 잔액 확인
    CashHistory lastHistory = cashHistoryRepository.findLastHistory().orElse(null);
    Double lastBalance = (lastHistory == null) ? 0 : lastHistory.getBalance();

    // 현금 잔액 update
    Double newBalance = lastBalance - amount;
    if(newBalance < 0) {
      throw new LackOfBalanceException(lastBalance, amount);
    }
*/

    // 현금 사용 기록 저장해야됨
    CashHistory newHistory = CashHistory.createPaidCashHistory(
        foreignAccount, amount, 0., store, transactionAt
    );

    // 저장
    cashHistoryRepository.save(newHistory);
    return amount;
  }

  /*
  * 현금 기록 조회
  */
  public List<CashHistory> findAllByForeignAccountAndPeriod(
      String accountNo, String startDate, String endDate
  ) {
    LocalDateTime start = DateUtil.getLocalDateTime(startDate);
    LocalDateTime end = DateUtil.getNextLocalDateTime(endDate);

    List<CashHistory> histories = cashHistoryRepository.findAllByForeignAccountAndPeriod(
      accountNo, start, end
    ).orElse(
        new ArrayList<CashHistory>()
    );

    return histories;
  }

  public List<CashHistory> findAllByForeignAccountOneDay(
      String accountNo, String date
  ) {
    List<LocalDateTime> stoe = getNextDate(date);

    List<CashHistory> histories = cashHistoryRepository.findAllByForeignAccountAndPeriod(
        accountNo, stoe.get(0), stoe.get(1)
    ).orElse(
        new ArrayList<CashHistory>()
    );

    return histories;
  }

  private List<LocalDateTime> getNextDate(String dateString) {
    // to LocalDateTime
    LocalDateTime start = DateUtil.getLocalDateTime(dateString);
    LocalDateTime end = start.plusDays(1);

    return List.of(start, end);
  }
}
