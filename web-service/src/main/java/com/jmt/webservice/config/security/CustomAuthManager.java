package com.jmt.webservice.config.security;


import com.jmt.webservice.model.AuthenticationRequest;
import com.jmt.webservice.service.WebAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Primary
public class CustomAuthManager implements ReactiveAuthenticationManager {

    private final WebAuthService webAuthService;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return webAuthService.authenticate(
                        new AuthenticationRequest(authentication.getName(), authentication.getCredentials().toString()))
                .log()
                .flatMap(authResponse -> Mono.just((Authentication) new UsernamePasswordAuthenticationToken(authentication.getName(),
                        authentication.getCredentials(), authentication.getAuthorities())))
                .log()
                .onErrorResume(e -> Mono.error(new BadCredentialsException("Invalid credentials!")));
    }
}

