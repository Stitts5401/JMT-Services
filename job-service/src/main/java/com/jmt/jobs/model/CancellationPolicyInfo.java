package com.jmt.jobs.model;

import com.jmt.jobs.entity.CancellationPolicy;
import lombok.Data;

@Data
public class CancellationPolicyInfo {
    private Integer id;
    private Integer jobId;
    private String details;
    private String days;
    public CancellationPolicyInfo(CancellationPolicy cancellationPolicy) {
        this.id = cancellationPolicy.getId();
        this.jobId = cancellationPolicy.getJobId();
        this.details = cancellationPolicy.getDetails();
        this.days = cancellationPolicy.getDays();
    }
}
