package com.jmt.userservice.model;


import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
