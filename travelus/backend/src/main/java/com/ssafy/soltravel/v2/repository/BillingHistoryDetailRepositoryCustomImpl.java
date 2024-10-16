package com.ssafy.soltravel.v2.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.soltravel.v2.domain.BillingHistoryDetail;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import com.ssafy.soltravel.v2.domain.QBillingHistoryDetail;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BillingHistoryDetailRepositoryCustomImpl implements BillingHistoryDetailRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public Optional<List<BillingHistoryDetail>> findByDynamicSettlementStatus(Long userId, SettlementStatus status) {
    QBillingHistoryDetail billingHistoryDetail = QBillingHistoryDetail.billingHistoryDetail;

    List<BillingHistoryDetail> results = queryFactory.selectFrom(billingHistoryDetail)
        .where(
            billingHistoryDetail.participant.user.userId.eq(userId),
            status == null ?
                null : // status가 null이면 조건 추가 안 함
                (status == SettlementStatus.COMPLETED ?
                    billingHistoryDetail.remainingAmount.loe(0) :
                    billingHistoryDetail.remainingAmount.gt(0)
                )
        )
        .fetch();
    return results.isEmpty() ? Optional.empty() : Optional.of(results);
  }
}
