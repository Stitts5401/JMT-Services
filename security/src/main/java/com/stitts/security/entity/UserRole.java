package com.stitts.security.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@Table("users_roles")
public class UserRole {
    @Column("user_id")
    private Long userId;
    @Column("role_id")
    private Long roleId;
}

