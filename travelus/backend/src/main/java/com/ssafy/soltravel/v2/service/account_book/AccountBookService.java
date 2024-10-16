package com.ssafy.soltravel.v2.service.account_book;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.domain.CashHistory;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistoryReadRequestDto;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistoryReadResponseDto;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistoryReadResponseDto.DayAccountHistory;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistorySaveRequestDto;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistorySaveRequestDto.Item;
import com.ssafy.soltravel.v2.dto.account_book.AccountHistorySaveResponseDto;
import com.ssafy.soltravel.v2.dto.account_book.DetailAccountHistoryReadRequestDto;
import com.ssafy.soltravel.v2.dto.account_book.DetailAccountHistoryReadResponseDto;
import com.ssafy.soltravel.v2.dto.account_book.ReceiptAnalysisDto;
import com.ssafy.soltravel.v2.dto.account_book.ReceiptUploadRequestDto;
import com.ssafy.soltravel.v2.dto.account_book.api.TransactionRequestBody;
import com.ssafy.soltravel.v2.dto.account_book.api.TransactionResponseBody;
import com.ssafy.soltravel.v2.dto.account_book.api.TransactionResponseBody.TransactionContent;
import com.ssafy.soltravel.v2.exception.LackOfBalanceException;
import com.ssafy.soltravel.v2.mapper.AccountBookMapper;
import com.ssafy.soltravel.v2.service.AwsFileService;
import com.ssafy.soltravel.v2.service.GPTService;
import com.ssafy.soltravel.v2.util.DateUtil;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountBookService {

    // 외부 API 호출 Service
    private final AwsFileService fileService;
    private final ClovaOcrService ocrService;
    private final GPTService gptService;

    // 내부 API
    private final CashHistoryService cashHistoryService;

    // 유틸
    private final SecurityUtil securityUtil;
    private final Map<String, String> apiKeys;
    private final WebClientUtil webClient;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;

    
    //------------------------------- 영수증 인식 -------------------------------------    
    public ReceiptAnalysisDto uploadReceipt(ReceiptUploadRequestDto requestDto)
        throws IOException {

        // 필요 변수 정의
        MultipartFile file = requestDto.getFile();
        Long userId = securityUtil.getCurrentUserId();

        // userId로 파일 저장(S3)
        String uploadUrl = fileService.saveReciept(file, userId);

        // Clova OCR 사용
        ResponseEntity<Map<String, Object>> response = ocrService.execute(requestDto, uploadUrl);

        // 챗지피티한테 정리시키기
        String receiptInfoString = gptService.askChatGPT(response.getBody().toString());
        LogUtil.info("RECeipte", receiptInfoString);

        // String(JSON) -> 객체 변환후 return
        return AccountBookMapper.convertJSONToItemAnalysisDto(receiptInfoString);
    }


    //------------------------------- 가계부 등록 -------------------------------------
    public AccountHistorySaveResponseDto saveAccountHistory(AccountHistorySaveRequestDto requestDto)
        throws LackOfBalanceException {

        cashHistoryService.payCash(requestDto);
        return AccountHistorySaveResponseDto.builder()
            .message("응답 메세지")
            .build();
    }


    //-------------------------------------------------------------------------------
    //------------------------------- 가계부 조회 -------------------------------------
    //-------------------------------------------------------------------------------
    public AccountHistoryReadResponseDto findAccountHistory(
        String accountNo, AccountHistoryReadRequestDto request
    ) {
        // 응답 변수 셋팅
        AccountHistoryReadResponseDto response = AccountHistoryReadResponseDto.create(accountNo);

        // 원화 거래 기록 조회 & 추가
        Map<String, Object> transactionHistory = requestTransactionHistory(
            accountNo,
            "KRW",
            request.getStartDate(),
            request.getEndDate()
        );
        updateMoneylogFromKRWTransactions(response, objectMapper.convertValue(transactionHistory, TransactionResponseBody.class));

        // 외화 거래 기록 조회 & 추가
        transactionHistory = requestTransactionHistory(
            accountNo,
            requestValidCurrencyCode(accountNo),
            request.getStartDate(),
            request.getEndDate()
        );
        updateMoneylogFromFRTransactions(response, objectMapper.convertValue(transactionHistory, TransactionResponseBody.class));

        // 현금 기록 조회 & 추가
        List<CashHistory> cashHistoryList = cashHistoryService.findAllByForeignAccountAndPeriod(
            accountNo,
            request.getStartDate(),
            request.getEndDate()
        );
        updateMoneylogFromCashHistory(response, cashHistoryList);

        return response;
    }


    //-------------------------------- 가계부 조회(유효 통화코드 조회) ----------------------------------
    private String requestValidCurrencyCode(String accountNo){
        Map<String, Object> body = new HashMap<>();
        body.put("Header", BankHeader.createHeader(apiKeys.get("API_KEY"), securityUtil.getUserByToken().getUserKey()));
        body.put("accountNo", accountNo);

        // 변환한 데이터로 이체 기록 요청
        ResponseEntity<Map<String, Object>> responseBody = webClient.request(
            "/accounts/inquireAccount",
            body,
            Map.class
        );

        Map<String, Object> recObject = (Map<String, Object>) responseBody.getBody().get("REC");
        List<Map<String, Object>> mbDtos = (List<Map<String, Object>>) recObject.get("moneyBoxDtos");
        return mbDtos.get(1).get("currencyCode").toString();
    }
    

    //-------------------------------- 가계부 조회(거래기록 조회) ----------------------------------
    private Map<String, Object> requestTransactionHistory(String accountNo, String currency, String start, String end) {
        // 요청 유저 파싱
        User user = securityUtil.getUserByToken();
        
        // (가계부 조회 요청 데이터)를 (모임 통장 이체 기록 요청 데이터)로 변환
        TransactionRequestBody requestBody = TransactionRequestBody.builder()
            .header(
                BankHeader.createHeader(
                    apiKeys.get("API_KEY"),
                    user.getUserKey()
                )
            )
            .currencyCode(currency)
            .accountNo(accountNo)
            .startDate(start)
            .endDate(end)
            .build();

        // 변환한 데이터로 이체 기록 요청
        ResponseEntity<Map<String, Object>> responseBody = webClient.request(
            "/transaction/history",
            requestBody,
            TransactionRequestBody.class
        );

        return (Map<String, Object>) responseBody.getBody().get("REC");
    }

    
    //-------------------------------- 가계부 조회(거래기록 -> 머니로그 원화) ----------------------------------
    private void updateMoneylogFromKRWTransactions(
        AccountHistoryReadResponseDto moneyLog,
        TransactionResponseBody transaction
    ) {
        List<DayAccountHistory> calendar = moneyLog.getMonthHistoryList();

        for(TransactionContent item : transaction.getContent()) {
            int date = item.getTransactionDate().getDayOfMonth();

            // 이게 소비인지 저축인지
            if(item.getTransactionType().contains("W")) {
                calendar.get(date).addTotalExpenditureKRW(item.getTransactionAmount());
            }else{
                calendar.get(date).addTotalIncomeKRW(item.getTransactionAmount());
            }
        }
    }

    //-------------------------------- 가계부 조회(거래기록 -> 머니로그 외화) ----------------------------------
    private void updateMoneylogFromFRTransactions(
        AccountHistoryReadResponseDto moneyLog,
        TransactionResponseBody transaction
    ) {
        List<DayAccountHistory> calendar = moneyLog.getMonthHistoryList();

        for(TransactionContent item : transaction.getContent()) {
            int date = item.getTransactionDate().getDayOfMonth();

            // 이게 소비인지 저축인지
            if(item.getTransactionType().contains("W")) {
                calendar.get(date).addTotalExpenditureForeign(item.getTransactionAmount());
            }else{
                calendar.get(date).addTotalIncomeForeign(item.getTransactionAmount());
            }
        }
    }

    //-------------------------------- 가계부 조회(현금기록 -> 머니로그) ----------------------------------
    private void updateMoneylogFromCashHistory(
        AccountHistoryReadResponseDto moneyLog,
        List<CashHistory> cashHistoryList
    ) {
        List<DayAccountHistory> calendar = moneyLog.getMonthHistoryList();
        for (CashHistory history : cashHistoryList) {
            int date = history.getTransactionAt().getDayOfMonth();
            calendar.get(date).addTotalExpenditureForeign(history.getAmount());
        }
    }


    //-------------------------------------------------------------------------------
    //----------------------------- 가계부 상세 조회 -----------------------------------
    //-------------------------------------------------------------------------------
    public List<DetailAccountHistoryReadResponseDto> findDetailAccountHistory(
        String accountNo,
        DetailAccountHistoryReadRequestDto request
    ) {
        // 응답 변수 셋팅
        List<DetailAccountHistoryReadResponseDto> response = new ArrayList<>();
        String start = request.getDate();
        String end = DateUtil.getNextLocalDateStr(start);

        // 원화 거래 기록 조회 & 추가
        Map<String, Object> transactionHistory = requestTransactionHistory(
            accountNo,
            "KRW",
            start,
            end
        );
        updateDetailLogFromTransactions(response, objectMapper.convertValue(transactionHistory, TransactionResponseBody.class));

        // 외화 거래 기록 조회 & 추가
        transactionHistory = requestTransactionHistory(
            accountNo,
            requestValidCurrencyCode(accountNo),
            start,
            end
        );
        updateDetailLogFromTransactions(response, objectMapper.convertValue(transactionHistory, TransactionResponseBody.class));

        // 현금 기록 조회 & 추가
        List<CashHistory> cashHistoryList = cashHistoryService.findAllByForeignAccountAndPeriod(
            accountNo,
            start,
            end
        );
        updateDetailLogFromCashHistory(response, cashHistoryList);

        return response;
    }

    //------------------------------ 가계부 상세 조회(거래기록 -> 머니로그) --------------------------------
    private void updateDetailLogFromTransactions(
        List<DetailAccountHistoryReadResponseDto> response,
        TransactionResponseBody transaction
    ){
        for(TransactionContent item : transaction.getContent()) {
            String payeeName, store, summary;
            if(item.getTransactionType().equals("CW")) {
                payeeName = "";
                store = item.getPayeeName();
                summary = "";
            }else{
                payeeName = item.getPayeeName();
                store = "";
                summary = item.getTransactionSummary();
            }

            DetailAccountHistoryReadResponseDto dto = DetailAccountHistoryReadResponseDto.builder()
                .transactionAt(item.getTransactionDate())
                .paid(item.getTransactionAmount())
                .payeeName(payeeName)
                .transactionSummary(summary)
                .store(store)
                .address("")
                .currency(item.getCurrencyCode().getCurrencyCode())
                .items(null)
                .build();
            response.add(dto);
        }
    }

    //------------------------------ 가계부 상세 조회(현금기록 -> 머니로그) --------------------------------
    private void updateDetailLogFromCashHistory(
        List<DetailAccountHistoryReadResponseDto> response,
        List<CashHistory> cashHistoryList
    ){
        for(CashHistory history : cashHistoryList) {
            List<Item> items = new ArrayList<>();
            history.getItemHistoryList().stream().forEach(item -> {
                items.add(
                    Item.builder()
                        .item(item.getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity().intValue())
                        .build()
                );
            });

            DetailAccountHistoryReadResponseDto dto = DetailAccountHistoryReadResponseDto.builder()
                .transactionAt(history.getTransactionAt())
                .paid(history.getAmount())
                .payeeName("")
                .transactionSummary("")
                .store(history.getStore())
                .address(history.getAddress())
                .currency(history.getCurrency().getCurrencyCode())
                .items(items)
                .build();

            response.add(dto);
        }
    }
}

