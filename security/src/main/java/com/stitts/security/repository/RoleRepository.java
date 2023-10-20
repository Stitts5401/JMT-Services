package com.stitts.security.repository;

import com.stitts.security.entity.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {

    @Query("SELECT r.* FROM users_roles ur JOIN roles r ON ur.role_id = r.role_id WHERE ur.user_id = :userId")
    Flux<Role> findRolesByUserId(@Param("userId") Long userId);
}