package com.ssafy.soltravel.v2.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "SolTravel API 명세서",
        description = "description",
        version = "api/v2"
    )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP).scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER).name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(Arrays.asList(securityRequirement));
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
            .group("All")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    public GroupedOpenApi accountApi() {
        return GroupedOpenApi.builder()
            .group("generalAccount")
            .pathsToMatch("/account/general/**")
            .build();
    }

    @Bean
    public GroupedOpenApi userManagementApi() {
        return GroupedOpenApi.builder()
            .group("userManagement")
            .pathsToMatch("/user/**")
            .build();
    }

    @Bean
    public GroupedOpenApi foreignAccountApi() {
        return GroupedOpenApi.builder()
            .group("foreignAccount")
            .pathsToMatch("/account/foreign/**")
            .build();
    }

    @Bean
    public GroupedOpenApi accountParticipantApi() {
        return GroupedOpenApi.builder()
            .group("Participant")
            .pathsToMatch("/account/*/participants")
            .build();
    }

    @Bean
    public GroupedOpenApi transactionApi() {
        return GroupedOpenApi.builder()
            .group("Transaction")
            .pathsToMatch("/transaction/**")
            .build();
    }

    @Bean
    public GroupedOpenApi authenticationApi() {
        return GroupedOpenApi.builder()
            .group("Authentication")
            .pathsToMatch("/auth/**")
            .build();
    }

    @Bean
    public GroupedOpenApi exchangeApi() {
        return GroupedOpenApi.builder()
            .group("Exchange")
            .pathsToMatch("/exchange/**")
            .build();
    }


}