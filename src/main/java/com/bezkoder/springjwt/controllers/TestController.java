package com.bezkoder.springjwt.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('ROLE_PEMBELI') or hasRole('ROLE_PENGELOLA') or hasRole('ROLE_ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/pengelola")
  @PreAuthorize("hasRole('ROLE_PENGELOLA')")
  public String pengelolaAccess() {
    return "Pengelola Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @GetMapping("/pembeli")
  @PreAuthorize("hasRole('ROLE_PEMBELI')")
  public String pembeliAccess() {
    return "Pembeli Board.";
  }
}