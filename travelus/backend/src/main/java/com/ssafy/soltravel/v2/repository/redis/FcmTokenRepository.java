package com.ssafy.soltravel.v2.repository.redis;

import com.ssafy.soltravel.v2.domain.redis.RedisFcm;
import org.springframework.data.repository.CrudRepository;

public interface FcmTokenRepository extends CrudRepository<RedisFcm, Long> {

}
