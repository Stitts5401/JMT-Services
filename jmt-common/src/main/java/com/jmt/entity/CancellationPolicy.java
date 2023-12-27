package com.jmt.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("cancellation_policy")
@Data
public class CancellationPolicy {
    @Id
    @Column("cancellation_policy_id")
    private Integer id;
    private Integer jobId;
    @Column("cancellation_details")
    private String details;
    @Column("cancellation_days")
    private String days;

    // Standard getters and setters
}
