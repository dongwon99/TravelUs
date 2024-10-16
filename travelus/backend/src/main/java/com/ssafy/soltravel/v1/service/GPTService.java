package com.ssafy.soltravel.v1.service;

import com.ssafy.soltravel.v1.util.LogUtil;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class GPTService {

  private final WebClient openAiWebClient;
  private Map<String, String> basePrompt = new HashMap<>();

  @PostConstruct
  public void init() {
    basePrompt.put("role", "system");
    basePrompt.put("content", "Receive the JSON receipt data and extract only the store name (store), "
        + "list of purchased items (items), item names (item), prices (price), quantity(quantity)(int, default: 1) and payment amount (paid). "
        + "Additionally, format the extracted information to ensure it becomes a complete JSON data."
    );
  }

  public String askChatGPT(String prompt) {

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "gpt-4o"); // 사용할 모델
    requestBody.put("response_format", Map.of("type", "json_object"));
    requestBody.put("messages", List.of(basePrompt, Map.of("role", "user", "content", prompt))); // 'messages' 필드 사용
    requestBody.put("max_tokens", 2048); // 최대 토큰 수 설정

    LogUtil.info(requestBody.toString());

    ResponseEntity<Map<String, Object>> response = openAiWebClient.post()
        .uri("/chat/completions")
        .bodyValue(requestBody) // 요청 본문 설정
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    if (response != null && response.getBody() != null) {
      Map<String, Object> responseBody = response.getBody();

      // OpenAI 응답 형식에 따라 처리 ('choices' 필드에서 텍스트 추출)
      if (responseBody.containsKey("choices")) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (!choices.isEmpty()) {
          Map<String, Object> choice = choices.get(0);
          if (choice.containsKey("message")) {
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            return (String) message.get("content");
          }
        }
      }
    }

    return null;
  }
}
