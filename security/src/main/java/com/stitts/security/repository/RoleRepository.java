package com.stitts.security.repository;

import com.stitts.security.entity.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
    // ... any custom query methods
}
