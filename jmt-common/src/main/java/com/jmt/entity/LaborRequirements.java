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
@Table("labor_requirements")
public class LaborRequirements {
    private Integer id;
    private Integer projectId;
    private String laborType;
    @Column("man_hours_estimated")
    private Integer manHoursEstimated;
}
