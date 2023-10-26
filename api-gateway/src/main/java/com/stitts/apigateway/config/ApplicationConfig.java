package com.stitts.apigateway.config;

import com.stitts.apigateway.auditing.ApplicationAuditAware;
import com.stitts.apigateway.config.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  @Bean
  public WebSessionServerSecurityContextRepository securityContextRepository() {
    return new WebSessionServerSecurityContextRepository();
  }
  @Bean
  public ReactiveAuditorAware<Integer> auditorAware() {
    return new ApplicationAuditAware();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  public ReactiveAuthenticationManager reactiveAuthenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    authenticationManager.setPasswordEncoder(passwordEncoder);
    return authenticationManager;
  }

}