package com.bezkoder.springjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private SignupRequest signupRequest;

    @BeforeEach
    public void setUp() {
        signupRequest = new SignupRequest();
    }

    @Test
    void testEmail() {
        signupRequest.setEmail("test@example.com");
        assertEquals("test@example.com", signupRequest.getEmail());
    }

    @Test
    void testPassword() {
        signupRequest.setPassword("password");
        assertEquals("password", signupRequest.getPassword());
    }

    @Test
    void testRole() {
        signupRequest.setRole("admin");
        assertEquals("admin", signupRequest.getRole());
    }

    @Test
    void testFirstName() {
        signupRequest.setFirstName("John");
        assertEquals("John", signupRequest.getFirstName());
    }

    @Test
    void testLastName() {
        signupRequest.setLastName("Doe");
        assertEquals("Doe", signupRequest.getLastName());
    }
}