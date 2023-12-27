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
@Table("risk_factors")
public class RiskFactors {
    private Integer id;
    private Integer projectId;
    @Column("risk_description")
    private String riskDescription;
}
