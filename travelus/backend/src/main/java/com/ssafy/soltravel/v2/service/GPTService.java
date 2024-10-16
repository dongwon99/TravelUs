package com.ssafy.soltravel.v2.service;

import com.ssafy.soltravel.v2.util.LogUtil;
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
  private Map<String, String> ICBasePrompt = new HashMap<>();

  @PostConstruct
  public void init() {
    basePrompt.put("role", "system");
    basePrompt.put("content", "\n"
        + "You are now an image data analyst. Your task is to parse data from receipt images using OCR technology. "
        + "Please extract the store name, total paid amount, transaction date and time, address, "
        + "transaction currency code, and transaction items (item name, quantity, price) from the receipt. "
        + "Then, provide the data in the following JSON format: {\n"
        + "   \"store\": \"Store Name\",\n"
        + "   \"paid\": \"Total Paid Amount\",\n"
        + "   \"transactionAt\": \"Transaction Date and Time(yyyy-MM-ddTHH:mm:ss)\",\n"
        + "   \"address\": \"Address\",\n"
        + "   \"currency\": \"Transaction Currency Code (e.g., USD for United States, JPY for Japan, based on the address)\",\n"
        + "   \"items\": [\n"
        + "      {\n"
        + "         \"item\": \"Item Name\",\n"
        + "         \"price\": \"Item Price\",\n"
        + "         \"quantity\": \"Item Quantity\"\n"
        + "      }\n"
        + "   ]\n"
        + "}\n"
        + "and Please exclude any symbols other than numbers and periods (.) in the price, quantity, and paid fields."
    );

    ICBasePrompt.put("role", "system");
    ICBasePrompt.put("content", "You are an image data analyst. "
        + "The following data is extracted from a South Korean ID card using OCR technology. "
        + "Please parse the Korean name and the resident registration number in the format YYMMDD-OOOOOOO from this ID card. "
        + "Please respond according to the following JSON format: { \"name\": \"Korean name\", \"residentRegistrationNumber\": \"Resident registration number\" }");
  }

  public String askChatGPT(String prompt) {

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "gpt-4o"); // 사용할 모델
    requestBody.put("response_format", Map.of("type", "json_object"));
    requestBody.put("messages", List.of(basePrompt, Map.of("role", "user", "content", prompt))); // 'messages' 필드 사용
    requestBody.put("max_tokens", 2048); // 최대 토큰 수 설정

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


  public String askChatGPTIC(String prompt) {

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "gpt-4o"); // 사용할 모델
    requestBody.put("response_format", Map.of("type", "json_object"));
    requestBody.put("messages", List.of(ICBasePrompt, Map.of("role", "user", "content", prompt))); // 'messages' 필드 사용
    requestBody.put("max_tokens", 2048); // 최대 토큰 수 설정

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
