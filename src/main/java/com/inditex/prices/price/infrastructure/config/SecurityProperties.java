package com.inditex.prices.price.infrastructure.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(List<PublicEndpoint> publicEndpoints) {

  public record PublicEndpoint(String path, String method) {}
}
