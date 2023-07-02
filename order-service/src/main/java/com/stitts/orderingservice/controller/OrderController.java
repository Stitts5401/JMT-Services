package com.stitts.orderingservice.controller;

import com.stitts.orderingservice.dto.OrderRequest;
import com.stitts.orderingservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderservice;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name= "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(()-> orderservice.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest , RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()-> "Oops! Something went wrong, please order after some time! - \n" + runtimeException.getMessage());
    }

}
