package com.stitts.apigateway.service;


import com.stitts.apigateway.config.security.JwtService;
import com.stitts.apigateway.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LogoutService implements ServerLogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
//        String authHeader = exchange.getExchange().getRequest().getHeaders().getFirst("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return Mono.empty();
//        }
//        String jwt = authHeader.substring(7);
//        String username = jwtService.extractUsername(jwt);
//        return tokenRepository.findTokenByUserId(username)
//                .flatMap(storedToken -> {
//                    storedToken.setExpired(true);
//                    storedToken.setRevoked(true);
//
//                    return tokenRepository.save(storedToken);
//                }).flatMap(storedToken -> {
//                    return ReactiveSecurityContextHolder.getContext()
//                            .map(securityContext -> {
//                                securityContext.setAuthentication(null);
//                                return securityContext;
//                            });
//                })
//                .then();
        return null;
    }
}

