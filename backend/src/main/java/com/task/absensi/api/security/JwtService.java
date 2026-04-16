package com.task.absensi.api.security;

import com.task.absensi.api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {
    private final String issuer;
    private final long expirationSeconds;
    private final SecretKey secretKey;

    public JwtService(
            @Value("${app.jwt.issuer:absensi-api}") String issuer,
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expirationSeconds:86400}") long expirationSeconds
    ) {
        this.issuer = issuer;
        this.expirationSeconds = expirationSeconds;
        this.secretKey = Keys.hmacShaKeyFor(toHmacKeyBytes(secret));
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(user.getUsername())
                .claim("uid", user.getId())
                .claim("role", user.getRole().name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(issuer)
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    private static byte[] toHmacKeyBytes(String secret) {
        if (secret == null) {
            throw new IllegalArgumentException("JWT secret is required");
        }
        String trimmed = secret.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("JWT secret is required");
        }

        if (looksLikeBase64(trimmed)) {
            byte[] decoded = Decoders.BASE64.decode(trimmed);
            if (decoded.length >= 32) {
                return decoded;
            }
        }

        return sha256(trimmed.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean looksLikeBase64(String value) {
        return value.matches("^[A-Za-z0-9+/=]+$") && value.length() % 4 == 0;
    }

    private static byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (Exception ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }
}
