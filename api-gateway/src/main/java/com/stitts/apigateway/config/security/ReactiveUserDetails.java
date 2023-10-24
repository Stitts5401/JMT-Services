package com.stitts.apigateway.config.security;


import com.stitts.apigateway.entity.Role;
import com.stitts.apigateway.entity.Token;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@Table(schema = "user")
public class ReactiveUserDetails implements UserDetails {

  @Id
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;
  private List<Token> tokens;
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return UserRules.valueOf(role.getName()).getAuthorities();
  }
  @Override
  public String getPassword() {
    return password;
  }
  @Override
  public String getUsername() {
    return email;
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
}
