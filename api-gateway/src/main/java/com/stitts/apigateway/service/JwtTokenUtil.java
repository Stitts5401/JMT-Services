package com.stitts.apigateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtTokenUtil {

    @Autowired
    private KeyStoreService keyStoreService;

    private Key SECRET_KEY;

    @PostConstruct
    public void init() {
        this.SECRET_KEY = keyStoreService.getJwtSigningKey();
    }
    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    public String getCredentialsSignatureFromToken(String token) {
        return extractAllClaims(token).get("credSig", String.class);
    }
    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // Include the credSig claim
        claims.put("credSig", DigestUtils.sha256Hex(userDetails.getUsername() + userDetails.getPassword()));

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))  // 10 hours validity
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

}
