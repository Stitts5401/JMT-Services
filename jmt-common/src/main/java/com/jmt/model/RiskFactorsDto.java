package com.jmt.model;

import com.jmt.entity.RiskFactors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class RiskFactorsDto {
    private Integer riskId;
    private Integer projectId;
    private String riskDescription;

    public RiskFactorsDto(RiskFactors riskFactors) {
        this.riskId = riskFactors.getId();
        this.projectId = riskFactors.getProjectId();
        this.riskDescription = riskFactors.getRiskDescription();
    }
}
