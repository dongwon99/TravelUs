package com.ssafy.soltravel.v1.domain.redis;

import com.ssafy.soltravel.v1.dto.exchange.Account;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("PreferenceRate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceRate {

  @Id
  private String id;//"USD(1437.8)"
  private Set<Account> accounts;
}
