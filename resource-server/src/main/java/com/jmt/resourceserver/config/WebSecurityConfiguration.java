package com.jmt.resourceserver.config;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
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

import java.util.Collection;
import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfiguration {

  @Value("${jwt.auth.converter.role-prefix}")
  private String rolePrefix;
  @Value("${jwt.auth.converter.jwt-realm-access-claim}")
  private String resourceAccess;
  @Value("${jwt.auth.converter.principal-attribute}")
  private String principleAttribute;
  @Value("${jwt.auth.converter.resource-id}")
  private String resourceId;
  @Value("${jwt.auth.converter.issuer-uri}")
  private String issuerUri;
  private final String[] WHITE_LIST = new String[]
          {
                  "/**",
                  "/home",
                  "/login/oauth2/code/*", "/oauth2/**", "/login/**", "/error/**"
          };
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf()
        .disable()
            .authorizeExchange(exchanges ->
                    exchanges
                            .matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            .pathMatchers(WHITE_LIST).permitAll()
                            .anyExchange().authenticated()
            )
            .oauth2Login()
            .and()
            .formLogin()
            .loginPage("/oauth2/authorization/keycloak")
            .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(customJwtAuthenticationConverter());

    return http.build();
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
        if (jwt.getClaim(resourceAccess) == null) {
          return Flux.fromIterable(grantedAuthorities);
        }
        JSONObject realmAccess = jwt.getClaim(resourceAccess);

        if (realmAccess.get(resourceId) == null) {
          return Flux.fromIterable(grantedAuthorities);
        }
        JSONArray roles = (JSONArray) realmAccess.get(resourceId);
        final List<SimpleGrantedAuthority> keycloakAuthorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(rolePrefix + role))
                .toList();
        grantedAuthorities.addAll(keycloakAuthorities);
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