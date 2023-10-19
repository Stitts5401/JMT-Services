package com.stitts.security.config;

import com.stitts.security.service.CustomReactiveUserDetailsService;
import com.stitts.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final CustomReactiveUserDetailsService customReactiveUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${spring.security.debug:false}")
    private boolean securityDebug;

    @Autowired
    public SecurityConfig(CustomReactiveUserDetailsService customReactiveUserDetailsService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.customReactiveUserDetailsService = customReactiveUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//       return http.csrf().disable()
//                .authorizeExchange(authz -> authz
//                        .pathMatchers("/static/**").permitAll()
//                        .pathMatchers("/login", "/login/**").permitAll()
//                        .pathMatchers("/user/**").hasRole("USER")
//                        .pathMatchers("/contractor/**").hasRole("CONTRACTOR")
//                        .pathMatchers("/create/**").hasRole("CREATOR")
//                        .pathMatchers("/admin/**").hasRole("ADMIN")
//                        .anyExchange().authenticated())
//                .httpBasic()
//                .and()
//                .formLogin(form -> form.loginPage("/login"))
//               .authenticationManager(reactiveAuthenticationManager())
//               .build();
//    }
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {

        final RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
        logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/index.html"));

        return http
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .authorizeExchange()
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/user/**").hasRole("USER")
                .pathMatchers("/contractor/**").hasRole("CONTRACTOR")
                .pathMatchers("/create/**").hasRole("CREATOR")
                // TODO * This duplicity would not be needed if matchers had a "hasAnyRole" method,
                // TODO   which it has in the Spring MVC version...
                .pathMatchers("/shared/**")
                .access(
                        (auth, obj) ->
                                AuthorityReactiveAuthorizationManager.hasRole("USER").check(auth, obj)
                                        .flatMap(
                                                (decision) -> {
                                                    if (decision.isGranted()) {
                                                        return Mono.just(decision);
                                                    }
                                                    return AuthorityReactiveAuthorizationManager.hasRole("ADMIN").check(auth, obj);
                                                }))
                .anyExchange().permitAll() // Except for protected paths above, the rest will be open
                .and()
                .exceptionHandling()
                // TODO * There doesn't seem to be a way to specify a page for an Access Denied-type exception...
                // .accessDeniedPage("/403.html");
                .and()
                .build();

    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(customReactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

}
