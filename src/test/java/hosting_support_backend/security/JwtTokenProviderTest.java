package hosting_support_backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    private static final String SECRET =
            "dGhpcyBpcyBhIHNlY3JldCBrZXkgZm9yIGp3dCB0b2tlbiBzaWduaW5n";

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", 86400000L);
    }

    private UserDetails userDetails() {
        return User.withUsername("reda@example.com")
                .password("$2a$hashed")
                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                .build();
    }

    @Test
    void generateToken_createsValidTokenWithUserId() {
        String token = tokenProvider.generateToken(userDetails(), 1L);

        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
        assertEquals("reda@example.com", tokenProvider.getUsernameFromToken(token));
    }

    @Test
    void generateToken_withoutUserIdStillValid() {
        String token = tokenProvider.generateToken(userDetails());

        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
        assertEquals("reda@example.com", tokenProvider.getUsernameFromToken(token));
    }

    @Test
    void validateToken_rejectsGarbageInput() {
        assertFalse(tokenProvider.validateToken("not-a-token"));
        assertFalse(tokenProvider.validateToken(""));
        assertFalse(tokenProvider.validateToken(null));
    }

    @Test
    void validateToken_rejectsExpiredToken() {
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", -1000L);

        String expired = tokenProvider.generateToken(userDetails());

        assertFalse(tokenProvider.validateToken(expired));
    }
}