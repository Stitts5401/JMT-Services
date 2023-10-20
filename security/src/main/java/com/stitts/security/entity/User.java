package com.stitts.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data  // This includes @ToString, @EqualsAndHashCode, @Getter, @Setter and @RequiredArgsConstructor
@NoArgsConstructor  // Provides a no-arg constructor
@AllArgsConstructor
@Table("users")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @Column("user_id")
    private Long id;
    private String email;
    @Column("full_name")
    private String fullName;
    private transient String password;
    private transient Integer enabled;
    @Column("joined_date")
    private transient LocalDateTime joinedDate;
    @Column("last_active_date")
    private transient LocalDateTime lastActiveDate;
    // Assuming this is refactored for R2DBC
    private transient List<Role> roles;
}

