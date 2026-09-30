package com.inditex.prices.price.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  private final SecurityProperties securityProperties;

  public SecurityConfiguration(final SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Bean
  @Profile("local")
  public SecurityFilterChain localSecurityFilterChain(final HttpSecurity http) {
    this.configurePublicEndpoints(http);
    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
    return http.build();
  }

  @Bean
  @Profile("!local")
  public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http) {
    this.configurePublicEndpoints(http);
    return http.build();
  }

  private void configurePublicEndpoints(final HttpSecurity http) {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth -> {
              for (final SecurityProperties.PublicEndpoint endpoint :
                  this.securityProperties.publicEndpoints()) {
                if (endpoint.method() == null) {
                  auth.requestMatchers(endpoint.path()).permitAll();
                } else {
                  auth.requestMatchers(HttpMethod.valueOf(endpoint.method()), endpoint.path())
                      .permitAll();
                }
              }
              auth.anyRequest().denyAll();
            });
  }
}
