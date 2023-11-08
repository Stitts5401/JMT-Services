package com.stitts.security.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@Table("roles")
public class Role {
    @Id
    @Column("role_id")
    private Long id;
    private String name;
}

