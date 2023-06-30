package com.stitts.productservice.controller;

import com.stitts.productservice.dto.ProductRequest;
import com.stitts.productservice.dto.ProductResponse;
import com.stitts.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createInterestReport(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllInterestRequests(){
        return productService.getAllRequests();
    }
}
