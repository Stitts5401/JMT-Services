package com.stitts.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Value("${jwt.auth.converter.role-prefix}")
    private String rolePrefix;
    @Value("${jwt.auth.converter.jwt-realm-access-claim}")
    private String resourceAccess;
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;
    @Value("${jwt.auth.converter.jwt-uri}")
    private String issuerUri;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/**").permitAll() // Public endpoints
                        .pathMatchers("/api/**").authenticated() // Public endpoints
                        .anyExchange().authenticated() // Protected endpoints
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                        oAuth2ResourceServerSpec
                                .jwt(jwtSpec -> jwtSpec
                                        .jwtAuthenticationConverter(customJwtAuthenticationConverter())
                                )
                                .authenticationEntryPoint((exchange, ex) -> {
                                    // Redirect to /home for unauthorized requests
                                    ServerHttpResponse response = exchange.getResponse();
                                    response.setStatusCode(HttpStatus.FOUND);
                                    response.getHeaders().setLocation(URI.create("/home"));
                                    return response.setComplete();
                                })
                )
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter customJwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
        return new Converter<>() {
            @Override
            public Flux<GrantedAuthority> convert(Jwt jwt) {
                Collection<GrantedAuthority> grantedAuthorities = delegate.convert(jwt);
                log.info("jwt: {}", jwt);
                log.info("grantedAuthorities: {}", grantedAuthorities);

                // Access the JWT claims as a Map
                Map<String, Object> resourceAccessMap = jwt.getClaim(resourceAccess);

                if (resourceAccess != null) {
                    // Here you access the Map directly without casting it to JSONObject
                    Map<String, Object> appAccess = (Map<String, Object>) resourceAccessMap.get(resourceId);
                    if (appAccess != null) {
                        List<String> roles = (List<String>) appAccess.get("roles");
                        if (roles != null) {
                            roles.stream()
                                    .map(roleName -> new SimpleGrantedAuthority(rolePrefix + roleName))
                                    .forEach(grantedAuthorities::add);
                        }
                    }
                }
                ;
                return Flux.fromIterable(grantedAuthorities);
            }
        };
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder
                .withJwkSetUri(issuerUri)
                .jwsAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }

}




