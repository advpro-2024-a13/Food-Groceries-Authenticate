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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  final
  AuthenticationManager authenticationManager;

  final
  UserRepository userRepository;

  final
  RoleRepository roleRepository;

  final
  PasswordEncoder encoder;

  final
  JwtUtils jwtUtils;
  
  String roleNotFound = "Error: Role is not found.";

  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String role = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse(null);

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.id(),
            userDetails.email(),
            userDetails.firstName(),
            userDetails.lastName(),
            role));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()),
            signUpRequest.getFirstName(),
            signUpRequest.getLastName());

    String strRole = signUpRequest.getRole();
    Role role;

    if (strRole == null) {
      role = roleRepository.findByName(ERole.ROLE_PEMBELI)
              .orElseThrow(() -> new RuntimeException(roleNotFound));
    } else {
        role = switch (strRole) {
            case "admin" -> roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException(roleNotFound));
            case "pengelola" -> roleRepository.findByName(ERole.ROLE_PENGELOLA)
                    .orElseThrow(() -> new RuntimeException(roleNotFound));
            default -> roleRepository.findByName(ERole.ROLE_PEMBELI)
                    .orElseThrow(() -> new RuntimeException(roleNotFound));
        };
    }

    user.setRole(role);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @GetMapping("/getAll")
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userRepository.findAll());
  }

  @GetMapping("/getUserById")
  public ResponseEntity<?> getUserById(@RequestParam Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      return ResponseEntity.ok(optionalUser.get());
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not found!"));
    }
  }

  @GetMapping("/getUserRole")
  public ResponseEntity<?> getUserRole(@RequestParam Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      Role role = optionalUser.get().getRole();
      return ResponseEntity.ok(role);
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: User is not found!"));
    }
  }

  @GetMapping("/getUserByJwt")
  public ResponseEntity<UserDetailsImpl> getUserByJwt() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    return ResponseEntity.ok(userDetails);
  }
}