package com.stitts.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data  // This includes @ToString, @EqualsAndHashCode, @Getter, @Setter and @RequiredArgsConstructor
@NoArgsConstructor  // Provides a no-arg constructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    @Column("user_id")
    private Long id;
    private String email;
    @Column("full_name")
    private String fullName;
    private String password;
    private Integer enabled;
    @Column("joined_date")
    private LocalDateTime joinedDate;
    @Column("last_active_date")
    private LocalDateTime lastActiveDate;
    // Assuming this is refactored for R2DBC
    private Set<Role> roles;
    public void setRoles(Collection<Role> roles) {
        this.roles = new HashSet<>(roles);
    }
}

