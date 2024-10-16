package com.ssafy.soltravel.v1.service.user;


import com.ssafy.soltravel.v1.domain.Enum.AccountType;
import com.ssafy.soltravel.v1.domain.User;
import com.ssafy.soltravel.v1.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.v1.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.v1.dto.user.UserDetailDto;
import com.ssafy.soltravel.v1.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.v1.dto.user.UserSearchResponseDto;
import com.ssafy.soltravel.v1.dto.user.api.UserCreateRequestBody;
import com.ssafy.soltravel.v1.exception.UserNotFoundException;
import com.ssafy.soltravel.v1.mapper.UserMapper;
import com.ssafy.soltravel.v1.repository.UserRepository;
import com.ssafy.soltravel.v1.service.AwsFileService;
import com.ssafy.soltravel.v1.service.NotificationService;
import com.ssafy.soltravel.v1.service.account.AccountService;
import com.ssafy.soltravel.v1.util.LogUtil;
import com.ssafy.soltravel.v1.util.PasswordEncoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final String API_URI = "/member";
  private final UserRepository userRepository;
  private final AwsFileService fileService;
  private final Map<String, String> apiKeys;
  private final WebClient webClient;
  private final AccountService accountService;
  private final NotificationService notificationService;


  // 외부 API 요청용 메서드
  private <T> ResponseEntity<Map<String, Object>> request(
      String uri,
      T requestBody, Class<T> bodyClass
  ) {
    return webClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(requestBody), bodyClass)
        .retrieve()
        // 에러 처리
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
            clientResponse.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .flatMap(body -> {
                  String responseMessage = body.get("responseMessage").toString();  // 원하는 메시지 추출
                  return Mono.error(new WebClientResponseException(
                      clientResponse.statusCode().value(),
                      responseMessage,  // 예외 메시지로 설정
                      clientResponse.headers().asHttpHeaders(),
                      responseMessage.getBytes(),  // 메시지를 바이트 배열로 변환
                      StandardCharsets.UTF_8 // 인코딩 지정
                  ));
                })
        )
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();
  }


  /*
  * 회원가입
  */ 
  public long createUser(UserCreateRequestDto createDto) throws IOException {

    // 외부 API 요청용 Body 생성(로그인)
    UserCreateRequestBody body = UserCreateRequestBody.builder()
        .apiKey(apiKeys.get("API_KEY"))
        .userId(createDto.getEmail())
        .build();

    // 외부 API 요청(로그인)
    LogUtil.info("request(create) to API", body);
    ResponseEntity<Map<String, Object>> response = request(
        API_URI, body, UserCreateRequestBody.class
    );

    // 외부 API 결과 저장(api key) 및 비밀번호 암호화
    String userKey = response.getBody().get("userKey").toString();
    createDto.setPassword(PasswordEncoder.encrypt(createDto.getEmail(), createDto.getPassword()));

    // 프로필 이미지 저장
    MultipartFile profile = createDto.getFile();
    String profileImageUrl = apiKeys.get("DEFAULT_PROFILE_URL");
    if(profile != null && !profile.isEmpty()) {
      profileImageUrl = fileService.saveProfileImage(profile);
    }

    // 저장할 수 있게 변환 후 저장
    User user = UserMapper.convertCreateDtoToUserWithUserKey(createDto, profileImageUrl, userKey);
    userRepository.save(user);
//    notificationService.subscribe(userId);

    /* 회원가입과 동시에 계좌 생성 필요 */
    // 외부 API 요청용 Body 생성(계좌 생성)
    CreateAccountRequestDto accountDto = CreateAccountRequestDto.builder()
        .accountType(AccountType.INDIVIDUAL)
        .accountPassword(createDto.getAccountPwd())
        .build();

    LogUtil.info("request(account) to API", accountDto);
    accountService.createGeneralAccount(user.getUserId(), accountDto);
    return user.getUserId();
  }


  /*
  * 사용자 계정 검색(리스트) 
  */
  public List<UserSearchResponseDto> searchAllUser(UserSearchRequestDto searchDto) {
    List<User> list = userRepository.findAll(searchDto).orElseThrow(
        () -> new UserNotFoundException(searchDto)
    );

    return list.stream().map(this::convertUserToSearchResponseDto).collect(Collectors.toList());
  }


  /*
  * 사용자 계정 검색(단건, userId) 
  */
  public UserSearchResponseDto searchOneUser(Long userId) {
    User user = userRepository.findByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );

    return convertUserToSearchResponseDto(user);
  }


  /*
  * 스프링 시큐리티 용
  */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new RuntimeException(String.format("loadUserByUsername Failed: %s", email))
    );

    return UserDetailDto.builder()
        .id(user.getUserId())
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .role(user.getRole())
        .build();
  }

  /*
   * 신한 API 사용하지 않는 회원가입
   */
  public void createUserWithoutAPI(UserCreateRequestDto createDto) throws IOException {
    
    // 비밀번호 암호화
    createDto.setPassword(PasswordEncoder.encrypt(createDto.getEmail(), createDto.getPassword()));

    // 프로필 이미지 저장
    MultipartFile profile = createDto.getFile();
    String profileImageUrl = null;
    if(profile != null && !profile.isEmpty()) {
      profileImageUrl = fileService.saveProfileImage(profile);
    }else {
      profileImageUrl = apiKeys.get("DEFAULT_PROFILE_URL");
    }

    User user = UserMapper.convertCreateDtoToUserWithUserKey(createDto, profileImageUrl);
    userRepository.save(user);
  }

  // 유저 엔티티를 조회 응답 DTO로 변환
  private UserSearchResponseDto convertUserToSearchResponseDto(User user) {
    return UserSearchResponseDto.builder()
        .userId(user.getUserId())
        .name(user.getName())
        .email(user.getEmail())
        .phone(user.getPhone())
        .address(user.getAddress())
        .birth(user.getBirth())
        .registerAt(user.getRegisterAt())
        .isExit(user.getIsExit())
        .build();
  }
}
