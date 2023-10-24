package com.stitts.apigateway.config;

import com.stitts.apigateway.auditing.ApplicationAuditAware;
import com.stitts.apigateway.config.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final CustomUserDetailsService customUserDetailsService;
  @Bean
  public WebSessionServerSecurityContextRepository securityContextRepository() {
    return new WebSessionServerSecurityContextRepository();
  }
  @Bean
  public ReactiveUserDetailsService userDetailsService() {
    return username -> customUserDetailsService.findByUsername(username)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User not found"))));
  }
  @Bean
  public ReactiveAuditorAware<Integer> auditorAware() {
    return new ApplicationAuditAware();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}