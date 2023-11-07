package com.jmt.webservice.config;

import com.jmt.webservice.service.UserInfoService;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity()
@EnableReactiveMethodSecurity()
public class SecurityConfiguration {

    private final String[] WHITE_LIST = new String[]
            {
                    "/**",
                    "/home",
                    "/login/oauth2/code/*", "/oauth2/**", "/login/**", "/error/**"
            };
    private final UserInfoService userInfoService;

    @Bean
    SecurityWebFilterChain webFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable
                )
                .authorizeExchange(exchanges ->
                        exchanges
                                .matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .pathMatchers(WHITE_LIST).permitAll()
                                .anyExchange().authenticated()
                )
                .formLogin(formLoginSpec ->
                        formLoginSpec
                                .loginPage("/oauth2/authorization/keycloak")
                                .authenticationSuccessHandler(this::oauth2AuthenticationSuccessHandler)
                                .authenticationFailureHandler((webFilterExchange, exception) -> {
                                    // Handle the failure here
                                    log.info("authenticationFailureHandler: {}", exception.getMessage());
                                    webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                    return webFilterExchange.getExchange().getResponse().setComplete();
                                })
                )
                .oauth2Login(oAuth2LoginSpec ->
                        oAuth2LoginSpec
                                .authenticationSuccessHandler(this::oauth2AuthenticationSuccessHandler)
                                .authenticationFailureHandler(this::oauth2AuthenticationFailureHandler)
                )
                .build();
    }

    private Mono<Void> oauth2AuthenticationSuccessHandler(WebFilterExchange webFilterExchange, Authentication authentication) {
        return redirectUser(webFilterExchange);
    }
    private Mono<Void> redirectUser(WebFilterExchange webFilterExchange) {
        // Perform the redirection after successful OAuth2 login
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create("/welcome"));
        return response.setComplete();
    }
    private Mono<Void> oauth2AuthenticationFailureHandler(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        // Handle OAuth2 authentication failure here
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setLocation(URI.create("/oauth2/authorization/keycloak"));
        return response.setComplete();
    }

}

