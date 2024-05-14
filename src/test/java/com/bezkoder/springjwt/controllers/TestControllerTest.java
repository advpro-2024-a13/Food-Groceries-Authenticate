package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void allAccess() throws Exception {
        mockMvc.perform(get("/api/test/all"))
                .andExpect(status().isOk());
    }

    @Test
    void userAccess() throws Exception {
        mockAuthentication("ROLE_PEMBELI");
        mockMvc.perform(get("/api/test/user"))
                .andExpect(status().isOk());
    }

    @Test
    void pengelolaAccess() throws Exception {
        mockAuthentication("ROLE_PENGELOLA");
        mockMvc.perform(get("/api/test/pengelola"))
                .andExpect(status().isOk());
    }

    @Test
    void adminAccess() throws Exception {
        mockAuthentication("ROLE_ADMIN");
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void pembeliAccess() throws Exception {
        mockAuthentication("ROLE_PEMBELI");
        mockMvc.perform(get("/api/test/pembeli"))
                .andExpect(status().isOk());
    }

    private void mockAuthentication(String role) {
        User user = new User();
        user.setRole(new Role(ERole.valueOf(role)));
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setLastName("Doe");
        user.setFirstName("John");

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}