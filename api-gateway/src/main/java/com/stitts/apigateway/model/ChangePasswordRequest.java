package com.stitts.apigateway.model;

import lombok.*;

@Data
@Builder
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
