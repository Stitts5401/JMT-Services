package com.jmt.webservice.config;

import com.jmt.webservice.config.security.CustomAuthManager;
import com.jmt.webservice.config.security.CustomAuthenticationEntryPoint;
import com.jmt.webservice.config.security.CustomAuthenticationFailureHandler;
import com.jmt.webservice.config.security.RoleBasedAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

import java.net.URI;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableRedisWebSession
public class SecurityConfig {

    private final CustomAuthManager customAuthManager;

    public SecurityConfig(CustomAuthManager customAuthManager) {
        this.customAuthManager = customAuthManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        final RoleBasedAuthenticationSuccessHandler authenticationSuccessHandler =
                new RoleBasedAuthenticationSuccessHandler();

        final RedirectServerLogoutSuccessHandler logoutSuccessHandler =
                new RedirectServerLogoutSuccessHandler();
        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/home"));

        final CustomAuthenticationFailureHandler authenticationFailureHandler =
                new CustomAuthenticationFailureHandler("/login");


        return http
                .csrf().disable()
                .authenticationManager(customAuthManager)
                .authorizeExchange()
                .pathMatchers("/assets/**", "*/favicon.ico", "/**",
                        "/authenticate" ).permitAll()
                .and()
                .formLogin()
                .authenticationSuccessHandler(authenticationSuccessHandler)
                .authenticationFailureHandler(authenticationFailureHandler)
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .exceptionHandling()
                //.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .build();
    }
}
