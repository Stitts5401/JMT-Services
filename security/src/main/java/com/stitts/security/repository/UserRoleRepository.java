package com.stitts.security.repository;

import com.stitts.security.entity.UserRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, Long> {
}