package com.jmt.jobs.model;

import com.jmt.jobs.entity.ConfirmationPolicy;
import lombok.Data;

@Data
public class ConfirmationPolicyInfo {
    private Integer id;
    private Integer jobId;
    private String details;
    public ConfirmationPolicyInfo(ConfirmationPolicy confirmationPolicy) {
        this.id = confirmationPolicy.getId();
        this.jobId = confirmationPolicy.getJobId();
        this.details = confirmationPolicy.getDetails();
    }
}
