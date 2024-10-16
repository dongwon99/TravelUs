package com.goofy.tunabank.v1.provider;

import com.goofy.tunabank.v1.domain.Enum.KeyStatus;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.exception.auth.ApiKeyExpiredException;
import com.goofy.tunabank.v1.exception.auth.InvalidApiKeyException;
import com.goofy.tunabank.v1.repository.KeyRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyProvider {

  private final KeyRepository keyRepository;

  private static final SecureRandom secureRandom = new SecureRandom();
  private static final int API_KEY_LENGTH = 48;  // Base64 인코딩 하고 나면 64자
  private static final int USER_KEY_LENGTH = 32; // 32자

  public boolean validateKey(Key key) {
    // 활성 상태 검사
    if(key.getStatus() == KeyStatus.INACTIVE) {
      throw new InvalidApiKeyException(key.getValue());
    }

    // 만료 기간 검사
    if(key.getExpireAt().isBefore(LocalDateTime.now())) {
      throw new ApiKeyExpiredException(key.getValue(), key.getExpireAt());
    }
    return true;
  }

  private String generateSalt(){
    byte[] salt = new byte[16];
    secureRandom.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  public String generateApiKey() {
    byte[] keyValue = new byte[API_KEY_LENGTH];
    secureRandom.nextBytes(keyValue);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(keyValue);
  }

  public String generateUserKey() {
    return UUID.randomUUID().toString();
  }


}
