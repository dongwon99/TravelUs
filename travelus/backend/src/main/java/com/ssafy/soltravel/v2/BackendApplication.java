package com.ssafy.soltravel.v2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(servers = {@Server(url = "https://j11d209.p.ssafy.io/api/v2", description = "default server url"),
    @Server(url = "http://localhost:8082/api/v2", description = "LOCAL")
})
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@Profile("v2")
public class BackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }
}
