package com.bezkoder.springjwt.security.jwt;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.io.IOException;

import static org.mockito.Mockito.*;

class AuthEntryPointJwtTest {

    private AuthEntryPointJwt authEntryPointJwt;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationServiceException authException;

    @BeforeEach
    public void setUp() throws IOException {
        authEntryPointJwt = new AuthEntryPointJwt();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
        authException = new AuthenticationServiceException("Test Exception");
    }

    @Test
    void testCommence() throws IOException {
        when(request.getServletPath()).thenReturn("/testPath");

        authEntryPointJwt.commence(request, response, authException);

        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(request, times(1)).getServletPath();
    }
}