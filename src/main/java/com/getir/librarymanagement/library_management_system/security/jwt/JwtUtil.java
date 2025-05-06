package com.getir.librarymanagement.library_management_system.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for generating and validating JWT tokens.
 * Uses HMAC-SHA with a configurable secret key and expiration time.
 */
@Component
public class JwtUtil {

    private final Key key;
    private final long jwtExpirationInMillis;

    /**
     * Initializes JwtUtil with the provided secret key and expiration duration.
     *
     * @param secretKey             the secret key used for signing the JWT
     * @param jwtExpirationInMillis expiration time in milliseconds
     */
    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration}") long jwtExpirationInMillis) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtExpirationInMillis = jwtExpirationInMillis;
    }

    /**
     * Generates a JWT token with the specified username and role.
     *
     * @param username the subject of the token
     * @param role     the role to embed as a claim
     * @return signed JWT token
     */
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from the token.
     *
     * @param token the JWT token
     * @return username (subject)
     */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extracts the role claim from the token.
     *
     * @param token the JWT token
     * @return user role
     */
    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * Validates the token's integrity and expiration.
     *
     * @param token the JWT token
     * @return true if token is valid; false otherwise
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Malformed, expired, or tampered token
            return false;
        }
    }

    /**
     * Parses and returns claims from the token.
     * Throws exception if token is invalid or expired.
     *
     * @param token the JWT token
     * @return JWT claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}