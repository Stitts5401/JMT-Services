package com.stitts.apigateway.auditing;

import com.stitts.apigateway.config.security.ReactiveUserDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class ApplicationAuditAware implements ReactiveAuditorAware<Integer> {

    @Override
    public @NotNull Mono<Integer> getCurrentAuditor() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication != null && authentication.isAuthenticated()
                        && !(authentication instanceof AnonymousAuthenticationToken))
                .map(authentication -> (ReactiveUserDetails) authentication.getPrincipal())
                .map(ReactiveUserDetails::getId)
                .defaultIfEmpty(0);
    }
}
