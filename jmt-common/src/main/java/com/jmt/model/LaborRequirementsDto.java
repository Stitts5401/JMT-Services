package com.jmt.model;

import com.jmt.entity.LaborRequirements;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class LaborRequirementsDto {
    private Integer id;
    private Integer projectId;
    private String laborType;
    private Integer manHoursEstimated;

    public LaborRequirementsDto(LaborRequirements laborRequirements) {
        this.id = laborRequirements.getId();
        this.projectId = laborRequirements.getProjectId();
        this.laborType = laborRequirements.getLaborType();
        this.manHoursEstimated = laborRequirements.getManHoursEstimated();
    }
}
