package com.jmt.jobs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("refund_policy")
@Data
public class RefundPolicy {
    @Id
    @Column("refund_policy_id")
    private Integer id;
    private Integer jobId;
    @Column("refund_details")
    private String details;

    // Standard getters and setters
}
