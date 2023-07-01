package com.stitts.orderingservice.controller;

import com.stitts.orderingservice.dto.OrderRequest;
import com.stitts.orderingservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderservice;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name= "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderservice.placeOrder(orderRequest);
        return "Order placed successfully.";
    }

    public String fallbackMethod(OrderRequest orderRequest , RuntimeException runtimeException){
        return "Oops! Something went wrong, please order after some time! - \n" + runtimeException.getMessage();
    }

}
