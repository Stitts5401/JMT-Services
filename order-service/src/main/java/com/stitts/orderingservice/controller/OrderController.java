package com.stitts.orderingservice.controller;

import com.stitts.orderingservice.dto.OrderRequest;
import com.stitts.orderingservice.service.OrderService;
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
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderservice.placeOrder(orderRequest);
        return "Order placed successfully.";
    }
}
