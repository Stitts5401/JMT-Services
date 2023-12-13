package com.jmt.webservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.JSONObject;

@EqualsAndHashCode(callSuper = true)
@Data
public class PasswordChangeRequest extends JSONObject {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
