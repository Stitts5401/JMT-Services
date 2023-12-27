package com.jmt.model;

import com.jmt.entity.QualityRequirements;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class QualityRequirementsDto {
    private Integer qualityId;
    private Integer projectId;
    private String standards;
    private String inspectionProcedures;

    public QualityRequirementsDto(QualityRequirements qualityRequirements) {
        this.qualityId = qualityRequirements.getId();
        this.projectId = qualityRequirements.getProjectId();
        this.standards = qualityRequirements.getStandards();
        this.inspectionProcedures = qualityRequirements.getInspectionProcedures();
    }
}
