package com.stitts.apigateway.controller;

import com.stitts.apigateway.model.AuthenticationRequest;
import com.stitts.apigateway.model.AuthenticationResponse;
import com.stitts.apigateway.config.security.AuthenticationService;
import com.stitts.apigateway.model.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public Mono<ResponseEntity<AuthenticationResponse>> register(@RequestBody RegisterRequest request) {
    return authenticationService.register(request)
            .map(ResponseEntity::ok);
  }

  @PostMapping("/authenticate")
  public Mono<ResponseEntity<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request, ServerHttpRequest serverHttpRequest
    return authenticationService.va(request, serverHttpRequest)
            .map(ResponseEntity::ok);
  }

  @PostMapping("/refresh-token")
  public Mono<ResponseEntity<AuthenticationResponse>> refreshToken(ServerHttpRequest request) {
    return authenticationService.refreshToken(request);
  }
}
