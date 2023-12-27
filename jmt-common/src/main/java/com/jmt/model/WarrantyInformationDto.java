package com.jmt.model;

import com.jmt.entity.WarrantyInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class WarrantyInformationDto {
    private Integer warrantyId;
    private Integer projectId;
    private String warrantyDetails;

    public WarrantyInformationDto(WarrantyInformation warrantyInformation) {
        this.warrantyId = warrantyInformation.getId();
        this.projectId = warrantyInformation.getProjectId();
        this.warrantyDetails = warrantyInformation.getWarrantyDetails();
    }
}
