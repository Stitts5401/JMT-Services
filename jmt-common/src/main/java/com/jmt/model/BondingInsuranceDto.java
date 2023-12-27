package com.jmt.model;

import com.jmt.entity.BondingInsurance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class BondingInsuranceDto {
    private Integer bondingId;
    private Integer projectId;
    private String bondingRequirements;
    private String insuranceRequirements;

    public BondingInsuranceDto(BondingInsurance bondingInsurance) {
        this.bondingId = bondingInsurance.getId();
        this.projectId = bondingInsurance.getProjectId();
        this.bondingRequirements = bondingInsurance.getBondingRequirements();
        this.insuranceRequirements = bondingInsurance.getInsuranceRequirements();
    }
}
