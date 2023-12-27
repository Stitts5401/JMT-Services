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
@Table("compliance_regulations")
public class ComplianceRegulations {
    private Integer id;
    private Integer projectId;
    @Column("building_codes")
    private String buildingCodes;
    @Column("safety_standards")
    private String safetyStandards;
}
