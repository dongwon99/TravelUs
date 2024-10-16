package com.ssafy.soltravel.v1.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor

public class WebClientConfig {

  private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1";
  private final Map<String, String> apiKeys;

  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    return builder.baseUrl(BASE_URL).build();
  }

  @Bean
  public WebClient openAiWebClient(WebClient.Builder builder) {
    return builder
        .baseUrl("https://api.openai.com/v1")
        .defaultHeader("Authorization", "Bearer " + apiKeys.get("GPT_KEY"))
        .defaultHeader("Content-Type", "application/json")
        .build();
  }
}
