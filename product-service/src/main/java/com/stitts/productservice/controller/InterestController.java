package com.stitts.productservice.controller;

import com.stitts.productservice.dto.InterestRequest;
import com.stitts.productservice.dto.InterestResponse;
import com.stitts.productservice.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/interest")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createInterestReport(@RequestBody InterestRequest interestRequest){
        interestService.createProduct(interestRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InterestResponse> getAllInterestRequests(){
        return interestService.getAllRequests();
    }
}
