package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder, jwtUtils);
    }

    @Test
    void authenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
                .thenReturn(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        JwtResponse expectedResponse = new JwtResponse(jwtUtils.generateJwtToken(authentication),
                userDetails.id(),
                userDetails.email(),
                userDetails.firstName(),
                userDetails.lastName(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null)
                );

        JwtResponse actualResponse = (JwtResponse) response.getBody();

        assert actualResponse != null;
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getFirstName(), actualResponse.getFirstName());
        assertEquals(expectedResponse.getLastName(), actualResponse.getLastName());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        assertEquals(expectedResponse.getRole(), actualResponse.getRole());
        assertEquals(expectedResponse.getTokenType(), actualResponse.getTokenType());
        assertEquals(expectedResponse.getAccessToken(), actualResponse.getAccessToken());
    }

    @Test
    void registerUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setRole("admin");

        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        Role role = new Role(ERole.ROLE_ADMIN);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(role));

        User user = new User(signupRequest.getEmail(), "encodedPassword", signupRequest.getFirstName(), signupRequest.getLastName());
        user.setRole(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        assertInstanceOf(Map.class, responseEntity.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();

        assertEquals("User registered successfully!", responseBody.get("message"));

        assertInstanceOf(User.class, responseBody.get("user"));
        User responseUser = (User) responseBody.get("user");

        assertEquals(user.getEmail(), responseUser.getEmail());
        assertEquals(user.getPassword(), responseUser.getPassword());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
        assertEquals(user.getRole(), responseUser.getRole());
    }

    @Test
    void registerUserWithPengelolaRole() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setRole("pengelola");

        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        Role role = new Role(ERole.ROLE_PENGELOLA);
        when(roleRepository.findByName(ERole.ROLE_PENGELOLA)).thenReturn(Optional.of(role));

        User user = new User(signupRequest.getEmail(), "encodedPassword", signupRequest.getFirstName(), signupRequest.getLastName());
        user.setRole(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        assertInstanceOf(Map.class, responseEntity.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();

        assertEquals("User registered successfully!", responseBody.get("message"));

        assertInstanceOf(User.class, responseBody.get("user"));
        User responseUser = (User) responseBody.get("user");

        assertEquals(user.getEmail(), responseUser.getEmail());
        assertEquals(user.getPassword(), responseUser.getPassword());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
        assertEquals(user.getRole(), responseUser.getRole());
    }

    @Test
    void registerUserWithDefaultRole() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setRole("other");

        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        Role role = new Role(ERole.ROLE_PEMBELI);
        when(roleRepository.findByName(ERole.ROLE_PEMBELI)).thenReturn(Optional.of(role));

        User user = new User(signupRequest.getEmail(), "encodedPassword", signupRequest.getFirstName(), signupRequest.getLastName());
        user.setRole(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        assertInstanceOf(Map.class, responseEntity.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();

        assertEquals("User registered successfully!", responseBody.get("message"));

        assertInstanceOf(User.class, responseBody.get("user"));
        User responseUser = (User) responseBody.get("user");

        assertEquals(user.getEmail(), responseUser.getEmail());
        assertEquals(user.getPassword(), responseUser.getPassword());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
        assertEquals(user.getRole(), responseUser.getRole());
    }

    @Test
    void registerUserEmailExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setRole(ERole.ROLE_ADMIN.name());

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertEquals("Error: Email is already in use!", ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void registerUserWithNullRole() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setRole(null);

        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        Role role = new Role(ERole.ROLE_PEMBELI);
        when(roleRepository.findByName(ERole.ROLE_PEMBELI)).thenReturn(Optional.of(role));

        User user = new User(signupRequest.getEmail(), "encodedPassword", signupRequest.getFirstName(), signupRequest.getLastName());
        user.setRole(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        assertInstanceOf(Map.class, responseEntity.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();

        assertEquals("User registered successfully!", responseBody.get("message"));

        assertInstanceOf(User.class, responseBody.get("user"));
        User responseUser = (User) responseBody.get("user");

        assertEquals(user.getEmail(), responseUser.getEmail());
        assertEquals(user.getPassword(), responseUser.getPassword());
        assertEquals(user.getFirstName(), responseUser.getFirstName());
        assertEquals(user.getLastName(), responseUser.getLastName());
        assertEquals(user.getRole(), responseUser.getRole());
    }

    @Test
    void registerUserWithNonExistentRole() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setRole("nonexistent");

        when(encoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        when(roleRepository.findByName(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> authController.registerUser(signupRequest));

        assertEquals("Error: Role is not found.", exception.getMessage());
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("john.doe@example.com", "password", "John", "Doe"));
        users.add(new User("jane.doe@example.com", "password", "Jane", "Doe"));

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<User>> response = authController.getAllUsers();

        assertEquals(ResponseEntity.ok(users), response);
    }

    @Test
    void getUserById() {
        User user = new User("john.doe@example.com", "password", "John", "Doe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.getUserById(1L);

        assertEquals(ResponseEntity.ok(user), response);
    }

    @Test
    void getUserByIdNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.getUserById(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: User is not found!", ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void getUserRole() {
        User user = new User("john.doe@example.com", "password", "John", "Doe");
        Role role = new Role(ERole.ROLE_ADMIN);
        user.setRole(role);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.getUserRole(1L);

        assertEquals(ResponseEntity.ok(role), response);
    }

    @Test
    void getUserRoleNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.getUserRole(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: User is not found!", ((MessageResponse) Objects.requireNonNull(response.getBody())).getMessage());
    }

    @Test
    void getUserByJwt() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<UserDetailsImpl> response = authController.getUserByJwt();

        assertEquals(userDetails, response.getBody());
    }
}