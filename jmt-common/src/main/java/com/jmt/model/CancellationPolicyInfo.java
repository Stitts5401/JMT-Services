package com.jmt.model;

import com.jmt.entity.CancellationPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
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
