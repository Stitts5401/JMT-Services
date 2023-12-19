package com.jmt.jobs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("policies")
public class Policy {
    @Id
    private Integer id;
    private String category;
}
