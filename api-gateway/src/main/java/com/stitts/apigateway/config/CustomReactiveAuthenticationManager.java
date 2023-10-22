package com.stitts.apigateway.config;

import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.digest.DigestUtils;
import com.stitts.apigateway.service.CustomReactiveUserDetailsService;
import com.stitts.apigateway.service.JwtTokenUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenUtil jwtTokenUtil;

    private final CustomReactiveUserDetailsService userDetailsService;

    public CustomReactiveAuthenticationManager(JwtTokenUtil jwtTokenUtil, CustomReactiveUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        // Extract username and credentials signature from the token
        String username = jwtTokenUtil.getUsernameFromToken(token);
        String tokenCredentialsSignature = jwtTokenUtil.getCredentialsSignatureFromToken(token);


        return userDetailsService.findByUsername(username)
                .handle((userDetails, sink) -> {
                    // Generate the credentials signature for the current state of the user
                    String currentCredentialsSignature = generateCredentialsSignature(userDetails);

                    // Compare the token's credentials signature with the current one
                    if (!currentCredentialsSignature.equals(tokenCredentialsSignature)) {
                        sink.error(new BadCredentialsException("Invalid token or user credentials have changed"));
                        return;
                    }

                    sink.next(new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            null,
                            userDetails.getAuthorities()));
                });
    }

    private String generateCredentialsSignature(UserDetails userDetails) {
        // Example: hashing the username + password. You can add more details as required.
        return DigestUtils.sha256Hex(userDetails.getUsername() + userDetails.getPassword());
    }
}
