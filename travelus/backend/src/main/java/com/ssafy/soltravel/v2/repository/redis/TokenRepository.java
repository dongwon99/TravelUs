package com.ssafy.soltravel.v2.repository.redis;

import com.ssafy.soltravel.v2.domain.redis.RedisToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<RedisToken, Long> {

}

