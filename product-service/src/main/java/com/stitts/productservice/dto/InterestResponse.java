package com.stitts.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterestResponse {
    private String id;
    private String name;
    private String description;
    private String email;
    private Long phoneNumber;
}