// src/main/java/com/careerforge/security/JwtUtil.java
package com.careerforge.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    /*
     * HOW JWT WORKS — 3 steps:
     * 1. generateToken(user)  → creates a signed string token
     * 2. extractUsername(token) → reads the email embedded in the token
     * 3. isTokenValid(token, user) → checks signature + expiry
     *
     * Token format: xxxxx.yyyyy.zzzzz
     *   xxxxx = Header (algorithm)
     *   yyyyy = Payload (email, expiry, role)
     *   zzzzz = Signature (HMAC of header+payload using secret key)
     *
     * If ANYONE tampers with the payload, the signature check FAILS.
     * This is why JWT is secure without a database lookup.
     */

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails.getUsername(), expirationMs);
    }

    private String buildToken(Map<String, Object> extraClaims, String username, long expiry) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(
                Jwts.parser().verifyWith(getSigningKey()).build()
                        .parseSignedClaims(token).getPayload()
        );
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
