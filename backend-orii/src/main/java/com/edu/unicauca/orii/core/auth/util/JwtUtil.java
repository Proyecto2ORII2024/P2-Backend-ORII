package com.edu.unicauca.orii.core.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Método para generar el token
    public String generateToken(String email, String role, Long id, String faculty) {
        Key signingKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        String token = Jwts.builder()
                .claim("userId", id)
                .setSubject(email)
                .claim("role", role)
                .claim("faculty", faculty)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de expiración
                .signWith(signingKey)
                .compact();

        return token;
    }

    // Método para validar el token
    public boolean validateToken(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    // Extraer el correo electrónico del token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Extraer el rol del token
    public String extractRole(String token) {
        return (String) extractClaims(token).get("role");
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
