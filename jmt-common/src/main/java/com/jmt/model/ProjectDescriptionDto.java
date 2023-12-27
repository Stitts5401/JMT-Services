package com.jmt.model;

import com.jmt.entity.ProjectDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class ProjectDescriptionDto {

    private Integer id;
    private Integer jobId;
    private String description;
    private String objectives;
    private LocationDetailsDto locationDetails;
    private ProjectTimelineDto projectTimeline;
    private List<WorkBreakdownDto> workBreakdown;
    private List<MaterialsEquipmentDto> materialsEquipment;
    private List<LaborRequirementsDto> laborRequirements;
    private ComplianceRegulationsDto complianceRegulations;
    private SiteConditionsDto siteConditions;
    private QualityRequirementsDto qualityRequirements;
    private BudgetConstraintsDto budgetConstraints;
    private RiskFactorsDto riskFactors;
    private ChangeOrderProcessDto changeOrderProcess;
    private PaymentTermsDto paymentTerms;
    private BondingInsuranceDto bondingInsurance;
    private WarrantyInformationDto warrantyInformation;

    public ProjectDescriptionDto(ProjectDescription projectDescription, LocationDetailsDto locationDetails, ProjectTimelineDto projectTimeline, List<WorkBreakdownDto> workBreakdown, List<MaterialsEquipmentDto> materialsEquipment, List<LaborRequirementsDto> laborRequirements, ComplianceRegulationsDto complianceRegulations, SiteConditionsDto siteConditions, QualityRequirementsDto qualityRequirements, BudgetConstraintsDto budgetConstraints, RiskFactorsDto riskFactors, ChangeOrderProcessDto changeOrderProcess, PaymentTermsDto paymentTerms, BondingInsuranceDto bondingInsurance, WarrantyInformationDto warrantyInformation) {
        this.id = projectDescription.getId();
        this.jobId = projectDescription.getJobId();
        this.description = projectDescription.getDescription();
        this.objectives = projectDescription.getObjectives();
        this.locationDetails = locationDetails;
        this.projectTimeline = projectTimeline;
        this.workBreakdown = workBreakdown;
        this.materialsEquipment = materialsEquipment;
        this.laborRequirements = laborRequirements;
        this.complianceRegulations = complianceRegulations;
        this.siteConditions = siteConditions;
        this.qualityRequirements = qualityRequirements;
        this.budgetConstraints = budgetConstraints;
        this.riskFactors = riskFactors;
        this.changeOrderProcess = changeOrderProcess;
        this.paymentTerms = paymentTerms;
        this.bondingInsurance = bondingInsurance;
        this.warrantyInformation = warrantyInformation;
    }

}
