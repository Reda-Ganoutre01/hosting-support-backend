package hosting_support_backend.security;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// JwtTokenProvider.java - Handles JWT creation and validation
@Component
public class JwtTokenProvider {

    // Secret key loaded from application properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // Token expiration time in milliseconds
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // Generate a JWT token for an authenticated user
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // Build the signing key from the secret
        SecretKey key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );

        return Jwts.builder()
                // Subject is the username
                .subject(userDetails.getUsername())
                // Include roles as a custom claim
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .issuedAt(now)
                .expiration(expiryDate)
                // Sign with the strongest HMAC-SHA algorithm allowed by the key
                .signWith(key)
                .compact();
    }

    // Extract the username from a valid token
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    // Validate the token signature and expiration
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid or expired
            return false;
        }
    }

    // Parse and verify the token
    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
