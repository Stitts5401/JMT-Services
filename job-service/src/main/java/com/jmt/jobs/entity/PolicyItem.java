package com.jmt.jobs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("policy_items")
public class PolicyItem {

    @Id
    private Integer id;
    @Column("policy_id")
    private Integer policy;
    private String section;
    private String content;
}
