package com.stitts.apigateway.controller;

import com.stitts.apigateway.model.LoginForm;
import com.stitts.apigateway.service.AuthenticationService;
import com.stitts.apigateway.service.CustomReactiveUserDetailsService;
import com.stitts.apigateway.service.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<String>> authenticate(@RequestBody LoginForm loginForm) {
        return authenticationService.authenticate(loginForm)
                .onErrorResume(BadCredentialsException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage())));
    }
}