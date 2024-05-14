package com.bezkoder.springjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    void testEmail() {
        loginRequest.setEmail("test@example.com");
        assertEquals("test@example.com", loginRequest.getEmail());
    }

    @Test
    void testPassword() {
        loginRequest.setPassword("password");
        assertEquals("password", loginRequest.getPassword());
    }
}