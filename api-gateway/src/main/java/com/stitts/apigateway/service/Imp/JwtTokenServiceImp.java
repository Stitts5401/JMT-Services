package com.stitts.apigateway.service.Imp;

import com.stitts.apigateway.service.ITokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.jsonwebtoken.lang.Classes.getResourceAsStream;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImp implements ITokenService {

    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEYSTORE_PATH = "classpath:jwt.p12"; // Assuming you've put jwt.p12 in resources folder
    private static final String KEY_ALIAS = "jwtkey";

    @Value("${keystore.password}")
    private String KEYSTORE_PASSWORD;
    @Value("${key.password}")
    private String KEY_PASSWORD;
    private Key SECRET_KEY;

    @PostConstruct
    public void init() {
        this.SECRET_KEY = getJwtSigningKey();
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

    public Key getJwtSigningKey() {
        try {
            java.security.KeyStore keyStore = java.security.KeyStore.getInstance(KEYSTORE_TYPE);

            try (InputStream inputStream = getResourceAsStream(KEYSTORE_PATH)) {
                keyStore.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
            }

            return keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load key from keystore", e);
        }
    }

}
