package com.stitts.apigateway.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitts.apigateway.model.AuthenticationRequest;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class ServerWebExchangeAuthenticationConverter implements ServerAuthenticationConverter {
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    return Mono.just(body);
                })
                .flatMap(body -> {  // Change this line to use flatMap instead of map
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        AuthenticationRequest authRequest = mapper.readValue(body, AuthenticationRequest.class);
                        return Mono.just(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);  // This is now properly handled by flatMap
                    }
                });
    }
}
