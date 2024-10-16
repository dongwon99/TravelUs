package com.ssafy.soltravel.v1.repository.redis;


import com.ssafy.soltravel.v1.domain.redis.RedisToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<RedisToken, Long> {

}

