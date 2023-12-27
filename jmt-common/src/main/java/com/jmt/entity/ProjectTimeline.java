package com.jmt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("project_timeline")
public class ProjectTimeline {
    private Integer id;
    private Integer projectId;
    @Column("start_date")
    private Date startDate;
    @Column("end_date")
    private Date endDate;
}
