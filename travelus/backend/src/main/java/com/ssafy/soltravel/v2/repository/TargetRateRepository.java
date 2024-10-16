package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.TargetRate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TargetRateRepository extends JpaRepository<TargetRate, Long> {

  @Query("SELECT t FROM TargetRate t WHERE t.group.groupId = :groupId")
  Optional<TargetRate> findByGroupId(@Param("groupId") Long groupId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Transactional
  @Query("DELETE FROM TargetRate t WHERE t.group.groupId = :groupId")
  void deleteByGroupId(@Param("groupId") Long groupId);
}
