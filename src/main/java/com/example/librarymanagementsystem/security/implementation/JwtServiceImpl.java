package com.example.librarymanagementsystem.security.implementation;

import com.example.librarymanagementsystem.enums.Role;
import com.example.librarymanagementsystem.exception.JwtParsingException;
import com.example.librarymanagementsystem.security.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String secretToken;
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    @Override
    public String generateToken(Authentication authentication, Role role) {
        PatronDetailsImpl userDetails = (PatronDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        String fullName = userDetails.getFullName();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 60000 * 120);

        return Jwts.builder()
                .setSubject(email)
                .claim("name", fullName)
                .claim("email", email)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    @Override
    public Map<String, String> parseTokenClaims(String token) throws JwtParsingException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return new HashMap<>() {{
                put("name", claims.get("name", String.class));
                put("email", claims.get("email", String.class));
                put("role", claims.get("role", String.class));
            }};
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new JwtParsingException("Error parsing JWT token: " + e.getMessage());
        }
    }
}
