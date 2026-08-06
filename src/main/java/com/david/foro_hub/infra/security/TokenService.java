package com.david.foro_hub.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.david.foro_hub.domain.usuario.Usuario;

@Service
public class TokenService {
    private final String secret;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret must be configured");
        }
        this.secret = secret;
    }

    public String generarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("ForoHub")
                .withSubject(usuario.getCorreoElectronico())
                .withExpiresAt(fechaExpiracion())
                .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token JWT", exception);
        }
    }

    private Instant fechaExpiracion() {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-06:00"));
    }

    public Optional<String> getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            var subject = JWT.require(algoritmo)
                .withIssuer("ForoHub")
                .build()
                .verify(tokenJWT)
                .getSubject();
            return Optional.ofNullable(subject);
        } catch (JWTVerificationException exception){
            return Optional.empty();
        }
    }
}
