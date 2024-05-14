package com.bezkoder.springjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    public void setUp() {
        jwtResponse = new JwtResponse("token", 1L, "test@example.com", "John", "Doe", "admin");
    }

    @Test
    void testAccessToken() {
        jwtResponse.setAccessToken("newToken");
        assertEquals("newToken", jwtResponse.getAccessToken());
    }

    @Test
    void testTokenType() {
        jwtResponse.setTokenType("NewType");
        assertEquals("NewType", jwtResponse.getTokenType());
    }

    @Test
    void testId() {
        jwtResponse.setId(2L);
        assertEquals(2L, jwtResponse.getId());
    }

    @Test
    void testEmail() {
        jwtResponse.setEmail("newtest@example.com");
        assertEquals("newtest@example.com", jwtResponse.getEmail());
    }

    @Test
    void testFirstName() {
        jwtResponse.setFirstName("Jane");
        assertEquals("Jane", jwtResponse.getFirstName());
    }

    @Test
    void testLastName() {
        jwtResponse.setLastName("Smith");
        assertEquals("Smith", jwtResponse.getLastName());
    }

    @Test
    void testRole() {
        assertEquals("admin", jwtResponse.getRole());
    }
}