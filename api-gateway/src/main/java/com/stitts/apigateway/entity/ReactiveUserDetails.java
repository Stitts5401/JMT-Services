package com.stitts.apigateway.entity;


import com.stitts.apigateway.config.security.UserRules;
import com.stitts.apigateway.entity.Role;
import com.stitts.apigateway.entity.Token;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Table("user")
public class ReactiveUserDetails implements UserDetails {

    @Id
    private Integer id;
    private String email;
    @Column("first_name")
    private String firstname;
    @Column("last_name")
    private String lastname;
    private boolean enabled;
    private String password;
    @Column("joined_date")
    private Date created;
    @Column("last_active_date")
    private Timestamp updated;
    private Role role;
    private Token token;
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
