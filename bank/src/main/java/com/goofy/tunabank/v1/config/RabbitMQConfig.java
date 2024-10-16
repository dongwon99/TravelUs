package com.goofy.tunabank.v1.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  public static final String EXCHANGE_RATE_QUEUE = "exchange_rate_queue";

  @Bean
  public Queue exchangeRateQueue() {
    return new Queue(EXCHANGE_RATE_QUEUE);
  }
}
