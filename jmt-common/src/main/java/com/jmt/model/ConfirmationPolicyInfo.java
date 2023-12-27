package com.jmt.model;

import com.jmt.entity.ConfirmationPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
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
