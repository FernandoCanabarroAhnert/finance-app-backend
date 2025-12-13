package com.fernandocanabarro.finance_app_backend.shared.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
@RequiredArgsConstructor
public class ProjectSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeExchange(auth -> {
                    auth.pathMatchers("/actuator/health/**").permitAll();
                    auth.pathMatchers("/webjars/swagger-ui/**").permitAll();
                    auth.pathMatchers("/v3/api-docs/**").permitAll();
                    auth.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.anyExchange().authenticated();
                })
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri)
                .build();
    }

}
