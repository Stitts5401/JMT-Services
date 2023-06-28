package com.stitts.productservice.service;

import com.stitts.productservice.dto.InterestRequest;
import com.stitts.productservice.dto.InterestResponse;
import com.stitts.productservice.models.Interest;
import com.stitts.productservice.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterestService {

    private final InterestRepository interestRepository;

    public void createProduct(InterestRequest interestRequest){

        Interest interest = Interest.builder()
                .name(interestRequest.getName())
                .description(interestRequest.getDescription())
                .email(interestRequest.getEmail())
                .phoneNumber(interestRequest.getPhoneNumber())
                .build();

        interestRepository.save(interest);
        log.info("Interest Request {} is saved", interest.getId());
    }

    public List<InterestResponse> getAllRequests() {
        List<Interest> interests = interestRepository.findAll();

        return interests.stream().map(this::mapToInterestResponse).toList();
    }

    private InterestResponse mapToInterestResponse(Interest interest) {
        return InterestResponse.builder()
                .id(interest.getId())
                .name(interest.getName())
                .description(interest.getDescription())
                .email(interest.getEmail())
                .phoneNumber(interest.getPhoneNumber())
                .build();
    }
}
