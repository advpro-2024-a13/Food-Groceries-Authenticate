package com.bezkoder.springjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    void testId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void testEmail() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testPassword() {
        user.setPassword("password");
        assertEquals("password", user.getPassword());
    }

    @Test
    void testFirstName() {
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testLastName() {
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void testCreatedAt() {
        Long currentTime = System.currentTimeMillis();
        user.setCreatedAt(currentTime);
        assertEquals(currentTime, user.getCreatedAt());
    }

    @Test
    void testRole() {
        Role role = new Role(ERole.ROLE_ADMIN);
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void testConstructor() {
        User userWithParam = new User("test@example.com", "password", "John", "Doe");
        assertEquals("test@example.com", userWithParam.getEmail());
        assertEquals("password", userWithParam.getPassword());
        assertEquals("John", userWithParam.getFirstName());
        assertEquals("Doe", userWithParam.getLastName());
        assertNotNull(userWithParam.getCreatedAt());
    }
}