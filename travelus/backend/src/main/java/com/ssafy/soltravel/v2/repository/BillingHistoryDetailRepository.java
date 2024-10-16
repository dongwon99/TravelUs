package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.BillingHistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingHistoryDetailRepository extends JpaRepository<BillingHistoryDetail, Long>, BillingHistoryDetailRepositoryCustom{

}
