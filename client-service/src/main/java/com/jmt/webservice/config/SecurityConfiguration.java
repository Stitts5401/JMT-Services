package com.jmt.webservice.config;

import com.jmt.webservice.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity()
@EnableReactiveMethodSecurity()
public class SecurityConfiguration {

    private final String[] WHITE_LIST = new String[]
            {
                     "/oauth2/**", "/error/**", "/**"
            };
    private final UserInfoService userInfoService;

    @Bean
    SecurityWebFilterChain webFilterChain(ServerHttpSecurity http)  {
        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
                new WebSessionServerLogoutHandler(), new SecurityContextServerLogoutHandler()
        );

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
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
                                .authenticationFailureHandler(this::oauth2AuthenticationFailureHandler)
                )
                .oauth2Login(oAuth2LoginSpec ->
                        oAuth2LoginSpec
                                .authenticationSuccessHandler(this::oauth2AuthenticationSuccessHandler)
                                .authenticationFailureHandler(this::oauth2AuthenticationFailureHandler)
                )
                .logout( (logout) -> logout.logoutUrl("/oauth2/authorization/keycloak")  )

                .build();
    }

    private Mono<Void> oauth2AuthenticationSuccessHandler(WebFilterExchange webFilterExchange, Authentication authentication) {
        return redirectUser(webFilterExchange);
    }
    private Mono<Void> redirectUser(WebFilterExchange webFilterExchange) {
        // Perform the redirection after successful OAuth2 login
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create("/welcome-directory"));
        return response.setComplete();
    }
    private Mono<Void> oauth2AuthenticationFailureHandler(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        // Handle OAuth2 authentication failure here
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setLocation(URI.create("/oauth2/authorization/keycloak"));
        return response.setComplete();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails userDetails = User.withUsername("admin").password("admin").roles("ADMIN").build();
        return new MapReactiveUserDetailsService(List.of(userDetails));
    }
}

