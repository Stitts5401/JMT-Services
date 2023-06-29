package com.stitts.productservice.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "Interest")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    private String id;
    private String name;
    private String description;
    private String email;
    private Long phoneNumber;
}
