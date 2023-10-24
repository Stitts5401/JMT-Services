package com.stitts.apigateway.repository;

import com.stitts.apigateway.entity.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
    @Query("SELECT ur.* FROM user_role ur JOIN role r ON ur.role_id = r.role_id WHERE ur.user_id = :userId")
    Mono<Role> findRolesByUserId(@Param("userId") Integer userId);
}


