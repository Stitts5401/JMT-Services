package com.jmt.webservice.config.security;


import com.jmt.webservice.model.AuthenticationRequest;
import com.jmt.webservice.service.WebAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class CustomAuthManager implements ReactiveAuthenticationManager {

    private final WebAuthService webAuthService;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return webAuthService.authenticate(new AuthenticationRequest(authentication.getName(), authentication.getCredentials().toString()))
                .flatMap(authResponse -> {
                    // Extract authorities from the response
                    List<SimpleGrantedAuthority> authorities = authResponse.getBody().getAuthority();
                    // Create a new authenticated token with the returned authorities
                    return Mono.<Authentication>just(new UsernamePasswordAuthenticationToken(
                            authentication.getName(),
                            authentication.getCredentials(),
                            authorities
                    ));
                });
    }
}

