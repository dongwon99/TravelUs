package com.ssafy.soltravel.v2.service.user;


import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.domain.redis.RedisPhone;
import com.ssafy.soltravel.v2.dto.auth.AuthOcrIdCardRequestDto;
import com.ssafy.soltravel.v2.dto.auth.AuthOcrIdCardResponseDto;
import com.ssafy.soltravel.v2.dto.auth.AuthReissueRequestDto;
import com.ssafy.soltravel.v2.dto.auth.AuthReissueResponseDto;
import com.ssafy.soltravel.v2.dto.auth.AuthSMSSendRequestDto;
import com.ssafy.soltravel.v2.dto.auth.AuthSMSSendResponseDto;
import com.ssafy.soltravel.v2.dto.auth.AuthSMSVerificationRequestDto;
import com.ssafy.soltravel.v2.dto.auth.AuthSMSVerificationResponseDto;
import com.ssafy.soltravel.v2.dto.user.UserLoginRequestDto;
import com.ssafy.soltravel.v2.dto.user.UserLoginResponseDto;
import com.ssafy.soltravel.v2.exception.auth.InvalidAuthCodeException;
import com.ssafy.soltravel.v2.exception.auth.InvalidCredentialsException;
import com.ssafy.soltravel.v2.exception.user.PhoneNotFoundException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.mapper.AuthMapper;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.repository.redis.PhoneRepository;
import com.ssafy.soltravel.v2.service.GPTService;
import com.ssafy.soltravel.v2.service.NotificationService;
import com.ssafy.soltravel.v2.service.account_book.ClovaOcrService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PhoneRepository phoneRepository;
    private DefaultMessageService messageService;
    private final ClovaOcrService clovaOcrService;
    private final GPTService gptService;

    private final Map<String, String> apiKeys;
    private final String SERVICE_PHONE_NUM = "01062966409";

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(
            apiKeys.get("SMS_API_KEY"),
            apiKeys.get("SMS_SECRET_KEY"),
            "https://api.coolsms.co.kr"
        );
    }

    /*
     * 일반회원 로그인
     */
    public UserLoginResponseDto login(UserLoginRequestDto loginRequestDto) {

        // 아이디 & 비밀번호 설정
        String id = loginRequestDto.getId();
        String encryptedPwd = PasswordEncoder.encrypt(id, loginRequestDto.getPassword());

        // 일치 검사
        User user = userRepository.findByEmailAndPwd(id, encryptedPwd).orElseThrow(
            () -> new InvalidCredentialsException(loginRequestDto.getId())
        );

        //TODO: 정지(탈퇴) 회원 검증

        // 응답 설정
        UserLoginResponseDto response = tokenService.saveRefreshToken(user.getUserId());
        response.setName(user.getName());

        return response;
    }

    /*
     * 인증 문자 발송
     */
    public AuthSMSSendResponseDto sendSMSForVerification(AuthSMSSendRequestDto request) {

        // 메세지 기본 설정(from, to)
        Message message = new Message();
        message.setFrom(SERVICE_PHONE_NUM);
        message.setTo(request.getPhone());

        // 메세지 내용 설정
        String authCode = makeRandomAuthCode();
        message.setText(String.format("[솔레는 여행, 신나는 통장] 인증 코드는 '%s' 입니다.", authCode));

        // 메세지 전송
        SingleMessageSentResponse response = messageService.sendOne(
            new SingleMessageSendingRequest(message)
        );

        // 결과 저장
        phoneRepository.save(new RedisPhone(request.getPhone(), authCode));
        LogUtil.info("SMS Sent Successfully", response);
        return AuthSMSSendResponseDto.builder()
            .phone(request.getPhone())
            .statusMessage(response.getStatusMessage())
            .build();
    }

    /*
     * 랜덤 인증 코드 생성
     */
    public String makeRandomAuthCode() {
        Random r1 = new Random();
        Random r2 = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (r1.nextBoolean()) {
                randomNumber.append(Integer.toString(r2.nextInt(10)));
            } else {
                randomNumber.append((char) (r2.nextInt(26) + 97));
            }
        }
        return randomNumber.toString();
    }

    /*
     * 인증 코드 확인(일치 여부)
     */
    public AuthSMSVerificationResponseDto verifySMSAuthCode(AuthSMSVerificationRequestDto request) {

        RedisPhone user = phoneRepository.findById(request.getPhone()).orElseThrow(
            () -> new PhoneNotFoundException(request.getPhone())
        );

        if (request.getAuthCode().equals(user.getAuthCode())) {
            return AuthSMSVerificationResponseDto.builder()
                .phone(request.getPhone())
                .statusMessage("인증에 성공했습니다.")
                .build();
        } else {
            throw new InvalidAuthCodeException(request.getPhone(), request.getAuthCode());
        }
    }

    /*
     * refresh 토큰 재발급
     */
    public AuthReissueResponseDto reissueRefreshToken(AuthReissueRequestDto request) {

        // refresh 토큰 파싱
        Long userId = tokenService.getUserIdFromRefreshToken(request.getRefreshToken());

        // 유저 조회
        userRepository.findByUserId(userId).orElseThrow(
            () -> new UserNotFoundException(userId)
        );

        // 토큰 재발급 및 응답
        UserLoginResponseDto loginDto = tokenService.saveRefreshToken(userId);
        return AuthMapper.convertLoginToReissueDto(loginDto);
    }

    public String ocrIdCard(AuthOcrIdCardRequestDto request) throws IOException {
        ResponseEntity<Map<String, Object>> response = clovaOcrService.executeIdCard(request.getFile());
        String idCardInfoString = gptService.askChatGPTIC(response.getBody().toString());
        return idCardInfoString;
    }
}