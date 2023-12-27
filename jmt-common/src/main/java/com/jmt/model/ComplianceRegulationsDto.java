package com.jmt.model;

import com.jmt.entity.ComplianceRegulations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class ComplianceRegulationsDto {
    private Integer complianceId;
    private Integer projectId;
    private String buildingCodes;
    private String safetyStandards;

    public ComplianceRegulationsDto(ComplianceRegulations complianceRegulations) {
        this.complianceId = complianceRegulations.getId();
        this.projectId = complianceRegulations.getProjectId();
        this.buildingCodes = complianceRegulations.getBuildingCodes();
        this.safetyStandards = complianceRegulations.getSafetyStandards();
    }
}
