package com.flightaggregator.flight_aggregator_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Flight Aggregator API")
            .description("API for aggregating flight search results multiple providers")
            .version("1.0.0"))
        .servers(List.of(
            new Server().url("http://localhost:8082")
                .description("Development server")));
  }
}
