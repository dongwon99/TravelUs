package com.ssafy.soltravel.v2.service.account_book;

import com.ssafy.soltravel.v2.dto.account_book.ReceiptUploadRequestDto;
import com.ssafy.soltravel.v2.dto.account_book.api.NCPClovaRequestBody;
import com.ssafy.soltravel.v2.dto.auth.NCPIdCardRequestDto;
import com.ssafy.soltravel.v2.util.LogUtil;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class ClovaOcrService {

  private final Map<String, String> apiKeys;
  private WebClient webClient;
  private WebClient webClientIC;

  @PostConstruct
  public void init() {
    webClient = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    webClientIC = WebClient.builder()
        .baseUrl(apiKeys.get("NCP_CLOVA_ID_OCR_URL"))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .defaultHeader("X-OCR-SECRET", apiKeys.get("NCP_CLOVA_ID_OCR_SECRET_KEY"))
        .build();
  }

  private <T> ResponseEntity<Map<String, Object>> request(
      String url,
      String key,
      T requestBody,
      Class<T> bodyClass
  ) {
    return webClient.post()
        .uri(url)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header("X-OCR-SECRET", key)
        .body(Mono.just(requestBody), bodyClass)
        .retrieve()
        // 에러 처리
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
            clientResponse.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(body -> {
                  LogUtil.error("Clova API Error", body);
                  String responseMessage = body.get("message").toString();  // 원하는 메시지 추출
                  return Mono.error(new WebClientResponseException(
                      clientResponse.statusCode().value(),
                      responseMessage,                          // 예외 메시지 설정
                      clientResponse.headers().asHttpHeaders(), // 헤더 설정
                      responseMessage.getBytes(),               // 메시지를 바이트 배열로 변환
                      StandardCharsets.UTF_8                    // 인코딩 지정
                  ));
                })
        )
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();
  }

  public ResponseEntity<Map<String, Object>> execute(ReceiptUploadRequestDto requestDto, String url) throws IOException {

    // 요청 바디 생성
    NCPClovaRequestBody requestBody = createRequestBody(
        requestDto.getFile(),
        url,
        requestDto.getFormat(),
        requestDto.getFile().getName(),
        requestDto.getLang()
    );

    // 요청
    String requestUrl = requestDto.getLang().equals("ko") ? apiKeys.get("NCP_CLOVA_KR_OCR_URL") : apiKeys.get("NCP_CLOVA_OCR_URL");
    String requestKey = requestDto.getLang().equals("ko") ? apiKeys.get("NCP_CLOVA_KR_OCR_SECRET_KEY") : apiKeys.get("NCP_CLOVA_OCR_SECRET_KEY");
    LogUtil.info("NAVER CLOVA OCR REQUEST", requestUrl, requestKey);

    ResponseEntity<Map<String, Object>> response = request(
        requestUrl,
        requestKey,
        requestBody,
        NCPClovaRequestBody.class
    );
    return response;
  }


  public ResponseEntity<Map<String, Object>> executeIdCard(MultipartFile idCard) throws IOException {

    // 파일 확장자 얻기
    String format = getFileExtension(idCard);

    // 요청 바디로 사용할 MultiValueMap 생성
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", idCard.getResource());
    body.add("message", new NCPIdCardRequestDto.Message(format));

    // WebClient를 사용한 multipart/form-data 요청
    ResponseEntity<Map<String, Object>> response = webClientIC.post()
        .body(BodyInserters.fromMultipartData(body))
        .retrieve()

        // 에러 처리
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
            clientResponse.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(bodyResponse -> {
                  LogUtil.error("Clova API Error", bodyResponse);
                  String responseMessage = bodyResponse.get("message").toString();
                  return Mono.error(new WebClientResponseException(
                      clientResponse.statusCode().value(),
                      responseMessage,
                      clientResponse.headers().asHttpHeaders(),
                      responseMessage.getBytes(),
                      StandardCharsets.UTF_8
                  ));
                })
        )

        // 응답값 파싱
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();

    return response;
  }


  private NCPClovaRequestBody createRequestBody(MultipartFile file, String url,
      String format, String name, String lang) throws IOException {

    NCPClovaRequestBody body = new NCPClovaRequestBody();
    body.addImage(encodeToBase64(file), url, format, name, lang);
    return body;
  }

  private static String encodeToBase64(MultipartFile file) throws IOException {
    byte[] fileContent = file.getBytes();
    return Base64.getEncoder().encodeToString(fileContent);
  }

  public String getFileExtension(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    if (fileName != null && fileName.contains(".")) {
      return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    return ""; // 확장자가 없는 경우 빈 문자열 반환
  }
}
