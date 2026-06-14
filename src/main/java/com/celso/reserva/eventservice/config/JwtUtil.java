package com.celso.reserva.eventservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * Utilitario de validacao de JWT.
 * Compatible com tokens gerados pelo identity-service (mesma JWT_SECRET).
 *
 * Diferente do identity-service, este servico apenas VALIDA tokens,
 * nao os gera (geracao e responsabilidade exclusiva do identity).
 */
@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;

    public JwtUtil(@Value("${app.jwt.secret}") String secretKeyString) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}