package com.ssafy.soltravel.v2.service.account;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.soltravel.v1.dto.user.UserDetailDto;
import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.AddMoneyBoxRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.DeleteAccountRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountListRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.PasswordValidateRequestDto;
import com.ssafy.soltravel.v2.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.v2.dto.account.response.PasswordValidateResponseDto;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import com.ssafy.soltravel.v2.dto.moneyBox.response.AddMoneyBoxResponseDto;
import com.ssafy.soltravel.v2.dto.moneyBox.response.DeleteMoneyBoxResponseDto;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.mapper.AccountMapper;
import com.ssafy.soltravel.v2.repository.GeneralAccountRepository;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.ParticipantRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.service.WebClientService;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final Map<String, String> apiKeys;

    private final WebClientService webClientService;

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final GeneralAccountRepository generalAccountRepository;

    private final AccountMapper accountMapper;
    private final ObjectMapper objectMapper;

    private final SecurityUtil securityUtil;

    private final String BASE_URL = "/accounts/";
    //    private final GroupService groupService;
    private final GroupRepository groupRepository;

    // 신규 계좌 생성
    public AccountDto createGeneralAccount(
        CreateAccountRequestDto requestDto
    ) {

        User user = securityUtil.getUserByToken();

        Map<String, Object> body = new HashMap<>();
        String API_URL = BASE_URL + "postAccount";

        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        body.put("Header", header);
        body.put("accountType", requestDto.getAccountType());
        body.put("accountPassword", requestDto.getAccountPassword());
        body.put("bankId", requestDto.getBankId());

        // 일반 계좌 DB 저장 로직 유저 완성 시 추가
        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);

        // REC 부분을 Object 타입으로 받기
        Map<String, String> recObject = (Map<String, String>) response.getBody().get("REC");

        // recObject -> AccountDto 매핑
        AccountDto accountDto = objectMapper.convertValue(recObject, AccountDto.class);

        return accountDto;
    }

    // 현재 유저의 계좌 조회
    public AccountDto getByAccountNo(String accountNo) {
        User user = securityUtil.getUserByToken();
        return fetchAccountDto(accountNo, user);
    }

    // 다른 유저 계좌 조회 - 알림용
    public AccountDto getByAccountNo(String accountNo, User user) {
        User currentUser = (user != null) ? user : securityUtil.getUserByToken();
        return fetchAccountDto(accountNo, currentUser);
    }

    // 실제 계좌 조회 로직을 담당하는 메서드
    private AccountDto fetchAccountDto(String accountNo, User user) {
        String API_URL = BASE_URL + "inquireAccount";
        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", accountNo);

        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);
        Map<String, String> recObject = (Map<String, String>) response.getBody().get("REC");

        AccountDto accountDto = objectMapper.convertValue(recObject, AccountDto.class);

        String email = recObject.get("credentialId");
        User accountUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));

        accountDto.setUserName(accountUser.getName());

        return accountDto;
    }


    // 계좌 소유주 검색 - accountNo 기반
    public UserDetailDto getUserByAccountNo(String accountNo) {

        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + "inquireAccount";

        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        Map<String, Object> body = new HashMap<>();

        body.put("Header", header);
        body.put("accountNo", accountNo);

        // 특정 계좌 조회
        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);

        // REC 부분을 Object 타입으로 받기
        Map<String, String> recObject = (Map<String, String>) response.getBody().get("REC");

        // email 로 유저 이름 조회
        String email = recObject.get("credentialId");

        User accountUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        return UserDetailDto.builder()
            .id(accountUser.getUserId())
            .name(accountUser.getName())
            .build();
    }

    // 계좌 전체 조회 (userId로)
    public List<AccountDto> getAllByUserId(InquireAccountListRequestDto requestDto) {

        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + "inquireAccountList";

        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        Map<String, Object> body = new HashMap<>();

        body.put("Header", header);
        body.put("searchType", requestDto.getSearchType());

        // 특정 유저 전체 계좌 조회
        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);

        // REC 부분을 Object 타입으로 받기
        List<Map<String, Object>> recObjectList = (List<Map<String, Object>>) response.getBody().get("REC");

        List<AccountDto> accountDtoList = objectMapper.convertValue(recObjectList,
            new TypeReference<List<AccountDto>>() {
            });

        return accountDtoList;
    }

    // 계좌 신규 머니박스 추가
    public AddMoneyBoxResponseDto addMoneyBox(AddMoneyBoxRequestDto requestDto) {

        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + "addMoneyBox";

        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        Map<String, Object> body = new HashMap<>();

        body.put("Header", header);
        body.putAll(objectMapper.convertValue(requestDto, Map.class));

        // 머니박스 추가
        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);

        // WebClient 응답에서 REC 부분을 가져오기
        List<Map<String, Object>> recObjectList = (List<Map<String, Object>>) response.getBody().get("REC");

        // REC 데이터를 MoneyBoxDto 리스트로 변환
        List<MoneyBoxDto> moneyBoxDtos = objectMapper.convertValue(recObjectList,
            new TypeReference<List<MoneyBoxDto>>() {
            });

        TravelGroup group = groupRepository.findByGroupAccountNo(requestDto.getAccountNo());

        return new AddMoneyBoxResponseDto(group.getGroupId(), moneyBoxDtos);
    }

    public PasswordValidateResponseDto validatePassword(PasswordValidateRequestDto requestDto) {

        User user = securityUtil.getUserByToken();
        String API_URL = BASE_URL + "validate-password";

        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        Map<String, Object> body = new HashMap<>();

        body.put("Header", header);
        body.put("accountNo", requestDto.getAccountNo());
        body.put("accountPassword", requestDto.getAccountPassword());

        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);
        Object recObject = response.getBody().get("REC");
        PasswordValidateResponseDto responseDto = objectMapper.convertValue(recObject, PasswordValidateResponseDto.class);

        return responseDto;
    }

    // 머니박스 삭제
    public DeleteMoneyBoxResponseDto deleteMoneyBox(AddMoneyBoxRequestDto requestDto) {

        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + "deleteMoneyBox";

        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        Map<String, Object> body = new HashMap<>();

        body.put("Header", header);
        body.putAll(objectMapper.convertValue(requestDto, Map.class));

        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);

        // WebClient 응답에서 REC 부분을 가져오기
        Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");

        // REC 데이터를 MoneyBoxDto 리스트로 변환
        DeleteMoneyBoxResponseDto responseDto = objectMapper.convertValue(recObject, DeleteMoneyBoxResponseDto.class);

        // 변환된 리스트를 반환하거나, 다른 로직에 사용
        return responseDto;

    }


    // 계좌 삭제
    public DeleteAccountResponseDto deleteAccount(DeleteAccountRequestDto requestDto) {

        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + "deleteAccount";

        BankHeader header = BankHeader.createHeader(apiKeys.get("API_KEY"), user.getUserKey());

        Map<String, Object> body = new HashMap<>();

        body.put("Header", header);
        body.putAll(objectMapper.convertValue(requestDto, Map.class));

        ResponseEntity<Map<String, Object>> response = webClientService.sendRequest(API_URL, body);

        // WebClient 응답에서 REC 부분을 가져오기
        Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");

        // REC 데이터를 DeleteAccountResponseDto로 변환
        DeleteAccountResponseDto responseDto = objectMapper.convertValue(recObject, DeleteAccountResponseDto.class);

        // 변환된 리스트를 반환하거나, 다른 로직에 사용
        return responseDto;

    }

}
