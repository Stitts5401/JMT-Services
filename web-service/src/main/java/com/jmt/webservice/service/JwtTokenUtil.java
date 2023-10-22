package com.jmt.webservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    public String decodeRoleFromJwt(String token) {
        List<String> roles = getRolesFromToken(token);
        // Example logic: if multiple roles, pick the "highest" or return first for simplicity
        return roles.isEmpty() ? "USER" : roles.get(0);
    }

    public String determineRedirectPathBasedOnRole(String role) {
        switch(role) {
            case "ADMIN":
                return "/admin/dashboard";
            case "USER":
                return "/user/home";
            case "CONTRACTOR":
                return "/contractor/overview";
            case "CREATOR":
                return "/creator/workspace";
            default:
                return "/login";  // Or some default path
        }
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

}
