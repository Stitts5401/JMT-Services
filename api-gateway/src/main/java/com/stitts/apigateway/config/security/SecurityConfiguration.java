package com.stitts.apigateway.config.security;

import com.stitts.apigateway.model.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Inject the Spring-managed bean here

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // Authentication Converter
        AuthenticationWebFilter authenticationWebFilter = getAuthenticationWebFilter();
        http.authorizeExchange( exchange -> exchange
                .pathMatchers("/api/v1/auth/**").permitAll()
                .anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // JWT filter for other endpoints
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION); // Authentication filter for /api/v1/auth

        //TODO: Continue debugging authorization failure for authencationWebFilter

        return http.build();
    }

    @NotNull
    private AuthenticationWebFilter getAuthenticationWebFilter() {
        log.info("Inside getAuthenticationWebFilter");
        ServerWebExchangeAuthenticationConverter authenticationConverter = new ServerWebExchangeAuthenticationConverter();

        // AuthenticationWebFilter for /api/v1/auth
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);

        authenticationWebFilter.setRequiresAuthenticationMatcher(serverWebExchange ->
                ReactiveSecurityContextHolder.getContext()
                        .log()
                        .flatMap(ctx -> {
                            if (ctx.getAuthentication() != null && ctx.getAuthentication().isAuthenticated()) {
                                log.info("Inside getAuthenticationWebFilter - ctx.getAuthentication() != null && ctx.getAuthentication().isAuthenticated()");
                                return ServerWebExchangeMatcher.MatchResult.notMatch();
                            } else {
                                log.info("Inside getAuthenticationWebFilter - ctx.getAuthentication() == null || !ctx.getAuthentication().isAuthenticated()");
                                return ServerWebExchangeMatcher.MatchResult.match();
                            }
                        })
        );

        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);
        authenticationWebFilter.setAuthenticationSuccessHandler((webFilterExchange, authentication) -> {
            log.warn("Inside getAuthenticationWebFilter - authenticationSuccessHandler: "+ authentication.toString());
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.getHeaders().add("Authorization", "Bearer " + jwt);
            return response.setComplete();
        });

        log.info("Inside getAuthenticationWebFilter returning authenticationWebFilter");
        return authenticationWebFilter;
    }

}




