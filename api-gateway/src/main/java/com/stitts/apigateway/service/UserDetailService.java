package com.stitts.apigateway.service;

import com.stitts.apigateway.model.UserInfo;
import com.stitts.apigateway.repository.JobRepository;
import com.stitts.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public Mono<UserInfo> getUserInformationFromUsername(String email, Collection<? extends GrantedAuthority> authorities) {
        return userRepository.findByUsername(email)
                .flatMap(user -> jobRepository.findJobsById(user.getId())
                        .collectList()
                        .map(jobs -> new UserInfo(user, jobs, authorities.stream()
                                .map(GrantedAuthority::getAuthority).collect(Collectors.toList())))
                );
    }
}
