package com.stitts.apigateway.service.Imp;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.digest.DigestUtils;
import com.stitts.apigateway.service.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
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
@Primary
public class AuthenticationService implements ReactiveAuthenticationManager {
    private final ITokenService tokenService;
    private final UserService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        // Extract username and credentials signature from the token
        String username = tokenService.getUsernameFromToken(token);
        String tokenCredentialsSignature = tokenService.getCredentialsSignatureFromToken(token);

        return userDetailsService.findByUsername(username)
                .handle((userDetails, sink) -> {
                    // Generate the credentials signature for the current state of the user
                    String currentCredentialsSignature = generateCredentialsSignature(userDetails);
                    // Compare the token's credentials signature with the current one
                    if (!currentCredentialsSignature.equals(tokenCredentialsSignature)) {
                        sink.error(new BadCredentialsException("Invalid token or user credentials have changed"));
                        return;
                    }

                    sink.next(new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            null,
                            userDetails.getAuthorities()));
                });
    }


    private String generateCredentialsSignature(UserDetails userDetails) {
        // Example: hashing the username + password. You can add more details as required.
        return DigestUtils.sha256Hex(userDetails.getUsername() + userDetails.getPassword() + userDetails.getAuthorities());
    }
    public Mono<ResponseEntity<String>> validateCredentials(String username, String password) {
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