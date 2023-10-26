package com.stitts.apigateway.config.security;

import com.stitts.apigateway.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public Mono<ResponseEntity<AuthenticationResponse>> authenticate(String username, String password) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authRequest)
                .map(authentication -> {
                    // Assuming you have methods to generate access and refresh tokens
                    String accessToken = jwtService.generateToken(authentication.getName(), authentication.getAuthorities());
                    String refreshToken = jwtService.generateRefreshToken(authentication.getName());

                    return ResponseEntity.ok(AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build());
                });
    }
}