package com.jmt.jobs.model;

import com.jmt.jobs.entity.RefundPolicy;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
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
