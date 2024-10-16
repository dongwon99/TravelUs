package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.QAbstractHistory;
import com.goofy.tunabank.v1.domain.history.QCardHistory;
import com.goofy.tunabank.v1.domain.history.QTransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionHistoryCustomRepositoryImpl implements TransactionHistoryCustomRepository {

  private final JPAQueryFactory queryFactory;

  private JPAQuery<AbstractHistory> buildBaseQuery(TransactionHistoryListRequestDto requestDto) {
    QAbstractHistory abstractHistory = QAbstractHistory.abstractHistory;
    QTransactionHistory transactionHistory = QTransactionHistory.transactionHistory;
    QCardHistory cardHistory = QCardHistory.cardHistory;

    BooleanBuilder transactionCurrencyCodeCondition = createTransactionCurrencyCodeCondition(
        transactionHistory, requestDto.getCurrencyCode());
    BooleanBuilder cardCurrencyCodeCondition = createCardCurrencyCodeCondition(cardHistory,
        requestDto.getCurrencyCode());

    return queryFactory
        .select(abstractHistory)
        .from(abstractHistory)
        .leftJoin(transactionHistory).on(
            abstractHistory.id.eq(transactionHistory.id)
                .and(abstractHistory.transactionType.eq(transactionHistory.transactionType))
                .and(transactionHistory.moneyBox.account.accountNo.eq(requestDto.getAccountNo()))
                .and(transactionCurrencyCodeCondition)
        )
        .leftJoin(cardHistory).on(
            abstractHistory.id.eq(cardHistory.id)
                .and(abstractHistory.transactionType.eq(cardHistory.transactionType))
                .and(cardHistory.card.account.accountNo.eq(requestDto.getAccountNo()))
                .and(cardCurrencyCodeCondition)
        )
        .where(
            transactionHistory.moneyBox.account.accountNo.eq(requestDto.getAccountNo())
                .or(cardHistory.card.account.accountNo.eq(requestDto.getAccountNo())),
            transactionTypeEq(abstractHistory, requestDto.getTransactionType()),
            transactionDateRangeEq(abstractHistory, requestDto.getStartDate(),
                requestDto.getEndDate())
        )
        .orderBy(getOrderByExpression(abstractHistory, requestDto.getOrderByType()));
  }

  public Optional<Page<AbstractHistory>> findHistoryByAccountNo(TransactionHistoryListRequestDto requestDto, Pageable pageable) {
    JPAQuery<AbstractHistory> query = buildBaseQuery(requestDto);

    if (pageable != null) {
      query.offset(pageable.getOffset()).limit(pageable.getPageSize());
    }

    List<AbstractHistory> result = query.fetch();
    long total = result.size();

    Pageable effectivePageable = pageable != null
        ? pageable
        : PageRequest.of(0, total > 0 ? (int) total : 1);

    return Optional.ofNullable(new PageImpl<>(result, effectivePageable, total));
  }

  public Optional<List<AbstractHistory>> findHistoryByAccountNo(TransactionHistoryListRequestDto requestDto) {
    List<AbstractHistory> result = buildBaseQuery(requestDto).fetch();
    return Optional.ofNullable(result);
  }




  private BooleanBuilder transactionTypeEq(QAbstractHistory qAbstractHistory,
      TransactionType transactionType) {
    return transactionType != null ? new BooleanBuilder(
        qAbstractHistory.transactionType.eq(transactionType)) : new BooleanBuilder();
  }

  private BooleanBuilder transactionDateRangeEq(QAbstractHistory qAbstractHistory,
      LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null) {
      LocalDateTime startDateTime = startDate.atStartOfDay();
      LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
      return new BooleanBuilder(qAbstractHistory.transactionAt.between(startDateTime, endDateTime));
    }
    return new BooleanBuilder();
  }

  private OrderSpecifier<LocalDateTime> getOrderByExpression(QAbstractHistory qAbstractHistory,
      OrderByType orderByType) {
    return orderByType == OrderByType.ASC ? qAbstractHistory.transactionAt.asc()
        : qAbstractHistory.transactionAt.desc();
  }

  private BooleanBuilder createTransactionCurrencyCodeCondition(
      QTransactionHistory transactionHistory, CurrencyType currencyCode) {
    BooleanBuilder builder = new BooleanBuilder();

    if (currencyCode != null) {
      builder.and(transactionHistory.moneyBox.currency.currencyCode.eq(currencyCode));
    }

    return builder;
  }

  private BooleanBuilder createCardCurrencyCodeCondition(QCardHistory cardHistory,
      CurrencyType currencyCode) {
    BooleanBuilder builder = new BooleanBuilder();

    if (currencyCode != null) {
      builder.and(cardHistory.currency.currencyCode.eq(currencyCode));
    }

    return builder;
  }
}
