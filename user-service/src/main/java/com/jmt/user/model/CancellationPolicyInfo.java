package com.jmt.user.model;

import lombok.Data;

@Data
public class CancellationPolicyInfo {
    private Integer id;
    private Integer jobId;
    private String details;
    private String days;
}
