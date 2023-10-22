package com.stitts.apigateway.service;

import com.stitts.apigateway.model.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {

    @Autowired
    private CustomReactiveUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Mono<ResponseEntity<String>> authenticate(LoginForm loginForm) {
        return userDetailsService.findByUsername(loginForm.getUsername())
                .flatMap(userDetails -> {
                    if (passwordEncoder.matches(loginForm.getPassword(), userDetails.getPassword())) {
                        String token = jwtTokenUtil.generateToken(userDetails);
                        return Mono.just(ResponseEntity.ok(token));
                    } else {
                        return Mono.error(new BadCredentialsException("Invalid credentials"));
                    }
                });
    }
}