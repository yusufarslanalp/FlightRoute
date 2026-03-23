package com.example.flight.route.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = new byte[32];
        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = (byte) i;
        }
        String secret = Base64.getEncoder().encodeToString(keyBytes);
        jwtUtil = new JwtUtil(secret, 60L);
    }

    @Test
    void generateToken_parseToken_restoresSubjectAndRoles() {
        String token = jwtUtil.generateToken("alice", List.of("ROLE_USER", "ROLE_ADMIN"));

        Jws<Claims> parsed = jwtUtil.parseToken(token);

        assertThat(parsed.getBody().getSubject()).isEqualTo("alice");
        @SuppressWarnings("unchecked")
        List<String> roles = parsed.getBody().get("roles", List.class);
        assertThat(roles).containsExactly("ROLE_USER", "ROLE_ADMIN");
    }
}
