package org.jefino.cms.cmsplatform.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.jefino.cms.cmsplatform.identity.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * JWT token generation and validation service.
 */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-expiration-ms:900000}") long accessTokenExpirationMs,
            @Value("${app.jwt.refresh-expiration-ms:604800000}") long refreshTokenExpirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    /**
     * Generates access token with user claims.
     */
    public String generateAccessToken(User user, String tenantCode) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("displayName", user.getDisplayName())
                .claim("tenant", tenantCode)
                .claim("permissions", user.getAllPermissions())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessTokenExpirationMs)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Generates refresh token.
     */
    public String generateRefreshToken(UUID userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(refreshTokenExpirationMs)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates token and returns claims.
     */
    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts user ID from token.
     */
    public UUID getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        return UUID.fromString(claims.getSubject());
    }

    /**
     * Extracts tenant code from token.
     */
    public String getTenantFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("tenant", String.class);
    }

    /**
     * Extracts permissions from token.
     */
    @SuppressWarnings("unchecked")
    public Set<String> getPermissionsFromToken(String token) {
        Claims claims = validateToken(token);
        return Set.copyOf(claims.get("permissions", java.util.List.class));
    }

    /**
     * Checks if token is valid (not expired, valid signature).
     */
    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
