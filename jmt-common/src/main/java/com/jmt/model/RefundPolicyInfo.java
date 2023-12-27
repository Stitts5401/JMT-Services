package com.jmt.model;

import com.jmt.entity.RefundPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@NoArgsConstructor
public class RefundPolicyInfo {
    private Integer id;
    private Integer jobId;
    private String details;
    public RefundPolicyInfo(RefundPolicy refundPolicy) {
        this.id = refundPolicy.getId();
        this.jobId = refundPolicy.getJobId();
        this.details = refundPolicy.getDetails();
    }
}
