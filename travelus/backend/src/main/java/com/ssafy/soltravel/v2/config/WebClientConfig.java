package com.ssafy.soltravel.v2.config;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

  @Value("${team.private.baseUrl}")
  private String BASE_URL;

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
