package com.stitts.apigateway.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ITokenService {
    String getUsernameFromToken(String token);
    Claims extractAllClaims(String token);
    String getCredentialsSignatureFromToken(String token);
    List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token);
    String generateToken(UserDetails userDetails);
}
