package com.stitts.apigateway.controller;

import com.stitts.apigateway.model.AuthenticationRequest;
import com.stitts.apigateway.model.AuthenticationResponse;
import com.stitts.apigateway.config.security.AuthenticationService;
import com.stitts.apigateway.model.ErrorResponse;
import com.stitts.apigateway.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/authenticate")
  public Mono<ResponseEntity<?>> login(@RequestBody AuthenticationRequest authenticationRequest) {
    return authenticationService
            .authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .onErrorResume(
                    ex -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ErrorResponse.builder()
                                    .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                    .details(ex.getMessage())
                                    .build()))
            );
  }
}
