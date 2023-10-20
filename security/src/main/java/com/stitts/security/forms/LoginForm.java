package com.stitts.security.forms;

// using lombok create a class loginForm with two private fields email and password add getters and setters

import lombok.Data;
@Data
public class LoginForm {
    private String username;
    private String password;
}
