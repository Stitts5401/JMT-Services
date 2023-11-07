package com.jmt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  public boolean validateToken(String jwt, String userToken) {
    try {
      // First, check if the token is not expired
      if (isTokenExpired(jwt)) {
        return false;
      }
      // Validate the token
      Claims claims = extractAllClaims(jwt);
      return jwt.equals(userToken); // assuming you store the JWT itself as userToken in the database
    } catch (Exception e) {
      return false;
    }
  }

  public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", username);
    claims.put("role", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    return buildToken(claims, jwtExpiration);
  }
  public String generateRefreshToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", username);
    return buildToken(claims, refreshExpiration);
  }
  private String buildToken(Map<String, Object> claims, long expiration) {
    return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(claims.get("username").toString())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
