package com.ssafy.soltravel.v2.repository.redis;

import com.ssafy.soltravel.v2.domain.redis.RedisPhone;
import org.springframework.data.repository.CrudRepository;

public interface PhoneRepository extends CrudRepository<RedisPhone, String> {

}

