package com.ssafy.soltravel.v1.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "phone", timeToLive = 60 * 5)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisPhone {

  @Id
  private String phone;
  private String authCode;
}
