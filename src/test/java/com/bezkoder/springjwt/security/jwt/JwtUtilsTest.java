package com.bezkoder.springjwt.security.jwt;

import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        jwtUtils.setJwtSecret("bezKoderSecretKeyThatIsLongEnoughToMeetTheRequirement");
        jwtUtils.setJwtExpirationMs(60000);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        UserDetailsImpl user = new UserDetailsImpl(
                1L,
                "test@test.com",
                "test",
                "test",
                "password",
                authority
        );
        authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Test
    void testGenerateJwtToken() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        UserDetailsImpl user = new UserDetailsImpl(
                1L,
                "test@test.com",
                "test",
                "test",
                "password",
                authority
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
    }

    @Test
    void testGetEmailFromJwtToken() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        UserDetailsImpl user = new UserDetailsImpl(
                1L,
                "test@test.com",
                "test",
                "test",
                "password",
                authority
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication);
        String email = jwtUtils.getEmailFromJwtToken(token);

        assertEquals("test@test.com", email);
    }

    @Test
    void testValidateJwtToken() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        UserDetailsImpl user = new UserDetailsImpl(
                1L,
                "test@test.com",
                "test",
                "test",
                "password",
                authority
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication);
        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateJwtTokenWithMalformedToken() {
        String malformedToken = "malformedToken";
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    void testValidateJwtTokenWithExpiredToken() throws InterruptedException {
        jwtUtils.setJwtExpirationMs(1);
        String token = jwtUtils.generateJwtToken(authentication);
        Thread.sleep(2);
        assertFalse(jwtUtils.validateJwtToken(token));
        jwtUtils.setJwtExpirationMs(60000);
    }

    @Test
    void testValidateJwtTokenWithEmptyClaims() {
        String tokenWithoutClaims = Jwts.builder().signWith(jwtUtils.getJwtKey(), SignatureAlgorithm.HS384).compact();
        assertFalse(jwtUtils.validateJwtToken(tokenWithoutClaims));
    }

    @Test
    void testGetJwtKey() {
        assertNotNull(jwtUtils.getJwtKey());
    }
}