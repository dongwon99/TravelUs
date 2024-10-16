package com.ssafy.soltravel.v1.repository.redis;

import com.ssafy.soltravel.v1.domain.redis.PreferenceRate;
import org.springframework.data.repository.CrudRepository;

public interface PreferenceRateRepository extends CrudRepository<PreferenceRate, String> {
}
