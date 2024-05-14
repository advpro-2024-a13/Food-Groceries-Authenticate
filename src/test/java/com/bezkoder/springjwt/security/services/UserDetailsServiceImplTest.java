package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername() {
        Role role = new Role(ERole.ROLE_PEMBELI);
        User user = new User("john.doe@example.com", "password", "John", "Doe");
        user.setRole(role);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("john.doe@example.com");

        assertEquals("john.doe@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_NotFound() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("john.doe@example.com"));
    }
}