package com.company.route.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

class SwaggerConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("route-service").pathsToMatch("/api/**").build();
  }
}
