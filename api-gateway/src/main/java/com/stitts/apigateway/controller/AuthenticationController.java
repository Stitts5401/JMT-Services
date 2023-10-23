package com.stitts.apigateway.controller;

import com.stitts.apigateway.model.LoginForm;
import com.stitts.apigateway.service.Imp.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<String>> authenticate(@RequestBody LoginForm loginForm) {

        return authenticationService.validateCredentials(loginForm.getUsername(), loginForm.getPassword())
                .onErrorResume(BadCredentialsException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage())));
    }
}