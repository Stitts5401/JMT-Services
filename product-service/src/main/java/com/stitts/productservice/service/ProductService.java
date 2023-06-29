package com.stitts.productservice.service;

import com.stitts.productservice.dto.ProductRequest;
import com.stitts.productservice.dto.ProductResponse;
import com.stitts.productservice.models.Product;
import com.stitts.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .email(productRequest.getEmail())
                .phoneNumber(productRequest.getPhoneNumber())
                .build();

        productRepository.save(product);
        log.info("Interest Request {} is saved", product.getId());
    }

    public List<ProductResponse> getAllRequests() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .email(product.getEmail())
                .phoneNumber(product.getPhoneNumber())
                .build();
    }
}
