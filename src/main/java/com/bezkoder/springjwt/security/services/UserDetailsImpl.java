package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
  @Serial
  private static final long serialVersionUID = 1L;

  private final Long id;

  private final String email;

  private final String firstName;

  private final String lastName;

  private final String password;

  private Long createdAt;

  private String role;

  private final Collection<? extends GrantedAuthority> authority;

  public UserDetailsImpl(Long id, String email, String firstName, String lastName, String password,
                         Collection<? extends GrantedAuthority> authority) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.authority = authority;
  }

  public static UserDetailsImpl build(User user) {
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().name());

    return new UserDetailsImpl(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            List.of(authority));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authority;
  }

  public Long id() {
    return id;
  }

  public String email() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }

  @Override
  public String getUsername() {
    return email;
  }
}
