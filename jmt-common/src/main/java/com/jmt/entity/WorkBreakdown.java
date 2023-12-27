package com.jmt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("work_breakdown")
public class WorkBreakdown {
    private Integer id;
    private Integer projectId;
    @Column("task_description")
    private String taskDescription;
    private Integer duration;
}
