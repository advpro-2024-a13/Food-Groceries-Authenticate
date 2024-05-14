package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        User user = new User("john.doe@example.com", "password", "John", "Doe");
        user.setId(1L);
        user.setRole(new Role(ERole.ROLE_PEMBELI));
        userDetails = new UserDetailsImpl(1L, "john.doe@example.com", "John", "Doe", "password", new SimpleGrantedAuthority("ROLE_PEMBELI"));
    }

    @Test
    void getAuthorities() {
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_PEMBELI", authority.getAuthority());
    }

    @Test
    void id() {
        assertEquals(1L, userDetails.id());
    }

    @Test
    void email() {
        assertEquals("john.doe@example.com", userDetails.email());
    }

    @Test
    void getPassword() {
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void isAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void equals() {
        assertEquals(userDetails, userDetails);
        assertNotEquals(null, userDetails);
        assertNotEquals(userDetails, new Object());
    }

    @Test
    void equals2() {
        UserDetailsImpl userDetails1 = new UserDetailsImpl(1L, "john.doe@example.com", "John", "Doe", "password", new SimpleGrantedAuthority("ROLE_PENGELOLA"));
        UserDetailsImpl userDetails2 = new UserDetailsImpl(1L, "jane.doe@example.com", "Jane", "Doe", "password", new SimpleGrantedAuthority("ROLE_PENGELOLA"));
        UserDetailsImpl userDetails3 = new UserDetailsImpl(2L, "jane.doe@example.com", "Jane", "Doe", "password", new SimpleGrantedAuthority("ROLE_PENGELOLA"));

        assertEquals(userDetails1, userDetails2);
        assertNotEquals(userDetails1, userDetails3);
    }

    @Test
    void firstName() {
        assertEquals("John", userDetails.firstName());
    }

    @Test
    void lastName() {
        assertEquals("Doe", userDetails.lastName());
    }

    @Test
    void getUsername() {
        assertEquals("john.doe@example.com", userDetails.getUsername());
    }

    @Test
    void hashCodeTest() {
        assertEquals(userDetails.hashCode(), userDetails.hashCode());
    }
}