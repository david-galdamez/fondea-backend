package com.project.fondea.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.fondea.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt_secret}")
    private String secret;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    public String generateToken(UserDto user) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("id", user.getId().toString())
                .withClaim("role", user.getRole().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withIssuer("Fondea backend")
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Fondea backend")
                .build();
        return verifier.verify(token);
    }

    public String extractUserId(String token) {
        return validateToken(token).getClaim("id").asString();
    }

    public String extractRole(String token) {
        return validateToken(token).getClaim("role").asString();
    }
}
