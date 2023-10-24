package com.stitts.apigateway.config.security;

import com.stitts.apigateway.entity.Token;
import com.stitts.apigateway.exception.UserNotFoundException;
import com.stitts.apigateway.model.AuthenticationRequest;
import com.stitts.apigateway.model.AuthenticationResponse;
import com.stitts.apigateway.model.RegisterRequest;
import com.stitts.apigateway.repository.UserRepository;
import com.stitts.apigateway.token.TokenRepository;
import com.stitts.apigateway.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

;import static org.springframework.data.repository.util.ReactiveWrapperConverters.map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final CustomReactiveAuthManager customReactiveAuthManager;
    public Mono<ResponseEntity<AuthenticationResponse>> refreshToken(ServerHttpRequest request) {
        return Mono.justOrEmpty(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(refreshToken -> {
                    String userEmail = jwtService.extractUsername(refreshToken);
                    return repository.findByEmail(userEmail)
                            .filter(user -> jwtService.isTokenValid(refreshToken, user))
                            .flatMap(user -> {
                                String accessToken = jwtService.generateToken(user);
                                revokeAllUserTokens(user);
                                return saveUserToken(user, accessToken)
                                        .then(Mono.just(
                                                new AuthenticationResponse(accessToken, refreshToken)
                                        ));
                            });
                })
                .map(ResponseEntity::ok)
                .onErrorResume(UserNotFoundException.class, ex -> {
                    // Set error-specific fields on errorResponse
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse()));
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthenticationResponse()));
    }
  public Mono<AuthenticationResponse> register(RegisterRequest request) {
    var user = ReactiveUserDetails.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();
    return repository.save(user)
            .flatMap(savedUser -> {
              var jwtToken = jwtService.generateToken(savedUser);
              var refreshToken = jwtService.generateRefreshToken(savedUser);
              return saveUserToken(savedUser, jwtToken)
                      .thenReturn(AuthenticationResponse.builder()
                              .accessToken(jwtToken)
                              .refreshToken(refreshToken)
                              .build());
            });
  }
  private Mono<Void> saveUserToken(ReactiveUserDetails reactiveUserDetails, String jwtToken) {
    var token = Token.builder()
            .reactiveUserDetails(reactiveUserDetails)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    return tokenRepository.save(token).then();
  }
  private Mono<Void> revokeAllUserTokens(ReactiveUserDetails reactiveUserDetails) {
    return tokenRepository.findAllValidTokenByUser(reactiveUserDetails.getId())
            .flatMap(token -> {
              token.setExpired(true);
              token.setRevoked(true);
              return tokenRepository.save(token);
            }).then();
  }

  // The refreshToken method has an imperative style and needs to be restructured to be truly reactive.
  // This requires deeper changes including the context it's called in and the way the HTTP request/response are handled.
}
