package com.ssafy.soltravel.v2.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "fcmToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisFcm {

    @Id
    private Long userId;
    private String fcmToken;
}
