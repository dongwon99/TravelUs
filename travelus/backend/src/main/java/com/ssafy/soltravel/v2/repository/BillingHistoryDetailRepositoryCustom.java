package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.BillingHistoryDetail;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import java.util.List;
import java.util.Optional;

public interface BillingHistoryDetailRepositoryCustom {

  public Optional<List<BillingHistoryDetail>> findByDynamicSettlementStatus(Long userId, SettlementStatus status);
}
