package com.stitts.apigateway.repository;

import com.stitts.apigateway.entity.dto.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {

    @Query("SELECT r.* FROM users_roles ur JOIN roles r ON ur.role_id = r.role_id WHERE ur.user_id = :userId")
    Flux<Role> findRolesByUserId(@Param("userId") Long userId);
}
