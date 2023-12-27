package com.jmt.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("confirmation_policy")
@Data
public class ConfirmationPolicy {
    @Id
    @Column("confirmation_policy_id")
    private Integer id;
    private Integer jobId;
    @Column("confirmation_details")
    private String details;

    // Standard getters and setters, or you could use Lombok to generate them
}

