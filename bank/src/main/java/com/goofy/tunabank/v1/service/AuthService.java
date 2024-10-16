package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Enum.KeyType;
import com.goofy.tunabank.v1.domain.Enum.Role;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueReponseDto;
import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueRequestDto;
import com.goofy.tunabank.v1.exception.auth.ApiKeyLimitExceededException;
import com.goofy.tunabank.v1.exception.auth.NotAdminException;
import com.goofy.tunabank.v1.exception.user.UserNotFoundException;
import com.goofy.tunabank.v1.provider.KeyProvider;
import com.goofy.tunabank.v1.repository.KeyRepository;
import com.goofy.tunabank.v1.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;
  private final KeyProvider keyProvider;

  public ApiKeyIssueReponseDto issueApiKey(ApiKeyIssueRequestDto request) {

    // 1. 이메일 등록 유무 검증
    User user = userRepository.findByCredentialId(request.getManagerId()).orElseThrow(
        () -> new UserNotFoundException(request)
    );

    // 2. ADMIN 유저인지 확인
    if(user.getRole() != Role.ADMIN){
      throw new NotAdminException(request.getManagerId());
    }

    // 3. 발급한 API KEY 개수가 5개 이상인 경우 발급 X
    int len = filterApiKeyType(user.getKeys()).size();
    if(len >= 5){
      throw new ApiKeyLimitExceededException(request.getManagerId());
    }

    // 4. API KEY 발급
    String keyValue = keyProvider.generateApiKey();

    // 5. 저장
    Key key = Key.createKey(user, KeyType.API, keyValue);
    keyRepository.save(key);

    // 6. 응답
    return ApiKeyIssueReponseDto.builder()
        .managerId(request.getManagerId())
        .apiKey(keyValue)
        .createAt(LocalDateTime.now())
        .expireAt(LocalDateTime.now().plusYears(1))
        .build();
  }

  public List<Key> filterApiKeyType(List<Key> keys) {
    return keys.stream()
        .filter(key -> key.getType() == KeyType.API)
        .collect(Collectors.toList());
  }
}
