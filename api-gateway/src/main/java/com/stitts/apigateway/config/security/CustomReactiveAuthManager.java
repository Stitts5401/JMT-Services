package com.stitts.apigateway.config.security;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.digest.DigestUtils;
import com.stitts.apigateway.service.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor()
public class CustomReactiveAuthManager implements ReactiveAuthenticationManager {
    private final ITokenService tokenService;
    private final CustomUserDetailsService userDetailsService; // Renamed for clarity

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        String username = tokenService.getUsernameFromToken(token);
        String tokenCredentialsSignature = tokenService.getCredentialsSignatureFromToken(token);

        return userDetailsService.findUserByUsername(username) // Changed method name for clarity
                .handle((userDetails, sink) -> {
                    String currentCredentialsSignature = generateCredentialsSignature(userDetails);
                    if (!currentCredentialsSignature.equals(tokenCredentialsSignature)) {
                        sink.error(new BadCredentialsException("Invalid token or user credentials have changed"));
                        return;
                    }

                    sink.next(new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            userDetails.getPassword(),
                            userDetails.getAuthorities()));
                });
    }

    private String generateCredentialsSignature(UserDetails userDetails) {
        return DigestUtils.sha256Hex(userDetails.getUsername() + userDetails.getPassword() + userDetails.getAuthorities());
    }

    public Mono<ResponseEntity<String>> validateCredentials(String username, , String password) {
        return Mono.just(new UsernamePasswordAuthenticationToken(username, password))
                .flatMap(this::authenticate)
                .flatMap(authenticated -> {
                    log.info("User authenticated successfully: {}", username);
                    return Mono.just(ResponseEntity.ok(authenticated.getCredentials().toString()));
                })
                .onErrorResume(AuthenticationException.class,
                        e -> Mono.just(ResponseEntity.status(401).body(e.getMessage())));
    }
}
