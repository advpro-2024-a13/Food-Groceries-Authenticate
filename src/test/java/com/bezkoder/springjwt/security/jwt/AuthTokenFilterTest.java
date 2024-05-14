package com.bezkoder.springjwt.security.jwt;

import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.Mockito.*;

class AuthTokenFilterTest {

    private AuthTokenFilter authTokenFilter;
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);
        authTokenFilter = new AuthTokenFilter();
        authTokenFilter.setJwtUtils(jwtUtils);
        authTokenFilter.setUserDetailsService(userDetailsService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        userDetails = mock(UserDetails.class);
    }

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtils.validateJwtToken("token")).thenReturn(true);
        when(jwtUtils.getEmailFromJwtToken("token")).thenReturn("test@example.com");
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken("token");
        verify(jwtUtils, times(1)).getEmailFromJwtToken("token");
        verify(userDetailsService, times(1)).loadUserByUsername("test@example.com");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithException() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtils.validateJwtToken("token")).thenThrow(new RuntimeException("Test exception"));

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).validateJwtToken("token");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithoutAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}