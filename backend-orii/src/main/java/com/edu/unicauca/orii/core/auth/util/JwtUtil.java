package com.edu.unicauca.orii.core.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil implements IJwtUtils {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private String jwtToken;

    // Método para generar el token
    public String generateToken(String email, String role) {
        String  token =  Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de expiración
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        
        this.jwtToken = token;
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
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    @Override
    public String getEmail() {
        return extractEmail(this.jwtToken);  
    }
}
