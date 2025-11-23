package com.fernandocanabarro.finance_app_backend.shared.services;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class AuthService {

    public Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> (Jwt) ctx.getAuthentication().getPrincipal())
            .map(jwt -> jwt.getClaimAsString("sub"));
    }

}
