package com.stitts.apigateway.model;

import lombok.*;


@Data
@Builder
public class ErrorResponse {
    private String message;
    private String details;
}
