package com.stitts.security.repository;

import com.stitts.security.entity.UserRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Long> {
    // ... any custom query methods
}