package com.ssafy.soltravel.v2.service.user;


import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.user.ProfileUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserDetailDto;
import com.ssafy.soltravel.v2.dto.user.UserPwdUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserSearchResponseDto;
import com.ssafy.soltravel.v2.dto.user.UserUpdateRequestDto;
import com.ssafy.soltravel.v2.dto.user.api.UserCreateRequestBody;
import com.ssafy.soltravel.v2.dto.user.api.UserCreateRequestBody.Header;
import com.ssafy.soltravel.v2.dto.user.api.UserUpdateRequestBody;
import com.ssafy.soltravel.v2.exception.user.UserPwdInvalidException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.mapper.UserMapper;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.service.AwsFileService;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.PasswordEncoder;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
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

    private final Map<String, String> apiKeys;

    private final AwsFileService fileService;
    private final AccountService accountService;

    private final UserRepository userRepository;

    private final WebClient webClient;
    private final SecurityUtil securityUtil;
    private final WebClientUtil webClientUtil;

    private final String API_URI = "/user";

    // 외부 API 요청용 메서드
    private <T> ResponseEntity<Map<String, Object>> request(
        String uri,
        T requestBody,
        Class<T> bodyClass
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
                        String responseMessage = body.get("errorMessage").toString();  // 원하는 메시지 추출
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


    public ResponseDto checkDupUser(String id) {
        LogUtil.info("duplicate check", id);

        User user = userRepository.findByEmail(id).orElse(null);
        if (user == null) {
            return new ResponseDto("SUCCESS", "존재하지 않는 ID입니다.");
        } else {
            return new ResponseDto("FAIL", "존재하는 ID입니다.");
        }
    }

    /*
     * 회원가입
     */
    public long createUser(UserCreateRequestDto createDto) throws IOException {

        LogUtil.info("createDto", createDto);

        checkDupUser(createDto.getId());

        // 외부 API 요청용 Body 생성(로그인)
        UserCreateRequestBody body = UserCreateRequestBody.builder()
            .header(
                Header.builder()
                    .apiKey(apiKeys.get("API_KEY"))
                    .build()
            )
            .userId(createDto.getId())
            .userName(createDto.getName())
            .build();

        // 외부 API 요청(로그인)
        LogUtil.info("request(create) to API", body);
        ResponseEntity<Map<String, Object>> response = webClientUtil.request(
            String.format("%s/join", API_URI), body, UserCreateRequestBody.class
        );

        // 외부 API 결과 저장(api key) 및 비밀번호 암호화
        String userKey = response.getBody().get("userKey").toString();
        createDto.setPassword(PasswordEncoder.encrypt(createDto.getId(), createDto.getPassword()));

        // 프로필 이미지 저장
        MultipartFile profile = null; //createDto.getFile();
        String profileImageUrl = apiKeys.get("DEFAULT_PROFILE_URL");
        if (profile != null && !profile.isEmpty()) {
            profileImageUrl = fileService.saveProfileImage(profile);
        }

        // 저장할 수 있게 변환 후 저장
        User user = UserMapper.convertCreateDtoToUserWithUserKey(createDto, profileImageUrl, userKey);
        userRepository.save(user);
//    notificationService.subscribe(userId);

        return user.getUserId();
    }


    /*
     * 사용자 계정 검색(리스트)
     */
    public List<UserSearchResponseDto> searchAllUser(UserSearchRequestDto searchDto) {
        if(searchDto == null){
            searchDto = new UserSearchRequestDto();
        }

        UserSearchRequestDto finalSearchDto = searchDto;
        List<User> list = userRepository.findAll(searchDto).orElseThrow(
            () -> new UserNotFoundException(finalSearchDto)
        );

        return list.stream().map(this::convertUserToSearchResponseDto).collect(Collectors.toList());
    }


    /*
     * 사용자 계정 검색(단건, userId)
     */
    public UserSearchResponseDto searchOneUser() {
        Long userId = securityUtil.getCurrentUserId();
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
        createDto.setPassword(PasswordEncoder.encrypt(createDto.getId(), createDto.getPassword()));

        // 프로필 이미지 저장
        MultipartFile profile = null; //createDto.getFile();
        String profileImageUrl = null;
        if (profile != null && !profile.isEmpty()) {
            profileImageUrl = fileService.saveProfileImage(profile);
        } else {
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
            .id(user.getEmail())
            .phone(user.getPhone())
            .address(user.getAddress())
            .birth(user.getBirth())
            .registerAt(user.getRegisterAt())
            .isExit(user.getIsExit())
            .profileImg(user.getProfile())
            .gender(String.valueOf(user.getGender()))
            .build();
    }

    /*
    * 프로필 이미지 변경
    */
    public void updateUserProfile(ProfileUpdateRequestDto request) throws IOException {
        User user = securityUtil.getUserByToken();

        // 프로필 이미지 저장
        LogUtil.info("이미지", request.getProfileImg().getName());
        String profileImageUrl = fileService.saveProfileImage(request.getProfileImg());
        user.updateProfile(profileImageUrl);
    }

    /*
    * 유저 정보 변경
    */
    public String updateUser(UserUpdateRequestDto request) {
        User user = securityUtil.getUserByToken();

        // 이름 변경 요청
        UserUpdateRequestBody requestBody = UserUpdateRequestBody
            .builder()
            .header(
                BankHeader.createHeader(
                    apiKeys.get("API_KEY"),
                    user.getUserKey()
                )
            )
            .userName(request.getName())
            .build();

        request(
            API_URI + "/update",
            requestBody,
            UserUpdateRequestBody.class
        );

        // 이름 변경 (SERVICE)
        user.update(request);
        return user.getEmail();
    }

    /*
    * 유저 비밀번호 변경
    */
    public String updatePwdrequest(UserPwdUpdateRequestDto request) {
        User user = securityUtil.getUserByToken();

        // 비밀번호 검증
        String encryptedBefore = PasswordEncoder.encrypt(user.getEmail(), request.getBefore());
        if(!encryptedBefore.equals(user.getPassword())){
            throw new UserPwdInvalidException(request.getBefore());
        }
        
        // 비밀번호 변경
        user.updatePwd(PasswordEncoder.encrypt(user.getEmail(), request.getAfter()));
        return user.getEmail();
    }
}
