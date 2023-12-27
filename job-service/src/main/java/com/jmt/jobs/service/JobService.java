package com.jmt.jobs.service;

import com.jmt.entity.*;
import com.jmt.model.*;
import com.jmt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobImageRepository jobImageRepository;
    private final CancellationPolicyRepository cancellationPolicyRepository;
    private final ConfirmationPolicyRepository confirmationPolicyRepository;
    private final RefundPolicyRepository refundPolicyRepository;
    private final ProjectDescriptionRepository projectDescriptionRepository;
    private final BondingInsuranceRepository bondingInsuranceRepository;
    private final BudgetConstraintsRepository budgetConstraintsRepository;
    private final ChangeOrderProcessRepository changeOrderProcessRepository;
    private final ComplianceRegulationsRepository complianceRegulationsRepository;
    private final LaborRequirementsRepository laborRequirementsRepository;
    private final LocationDetailsRepository locationDetailsRepository;
    private final MaterialsEquipmentRepository materialsEquipmentRepository;
    private final PaymentTermsRepository paymentTermsRepository;
    private final ProjectTimelineRepository projectTimelineRepository;
    private final QualityRequirementsRepository qualityRequirementsRepository;
    private final RiskFactorsRepository riskFactorsRepository;
    private final SiteConditionsRepository siteConditionsRepository;
    private final WarrantyInformationRepository warrantyInformationRepository;
    private final WorkBreakdownRepository workBreakdownRepository;


    public Flux<Job> getJobsByUserId(Integer userId) {
        return jobRepository.findJobsById(userId).log();
    }
    public Flux<Job> get(int page, int size, String filter) {
        // Skipping (page * size) items and taking the next 'size' items from the Flux
        return jobRepository.findByFilter(filter)
                .skip((long) page * size)
                .take(size);
    }
    public Mono<JobInfo> getJobById(Integer jobId) {
        return jobRepository.findJobById(jobId)
                .flatMap(job -> Mono.zip(
                                confirmationPolicyRepository.findByJobId(job.getId()).collectList(),
                                cancellationPolicyRepository.findByJobId(job.getId()).collectList(),
                                refundPolicyRepository.findByJobId(job.getId()).collectList(),
                                jobImageRepository.findJobImagesById(jobId).collectList().defaultIfEmpty(Collections.emptyList()),
                                getProjectDescription(job.getProjectId())
                        )
                        .map(tuple -> {
                            // Unpack the results from Mono.zip into respective lists
                            List<ConfirmationPolicy> confirmationPolicies = tuple.getT1();
                            List<CancellationPolicy> cancellationPolicies = tuple.getT2();
                            List<RefundPolicy> refundPolicies = tuple.getT3();
                            List<JobImage> jobImages = tuple.getT4();
                            ProjectDescriptionDto projectDescriptionDto = tuple.getT5();
                            // Convert policies and job images to their respective DTOs
                            List<ConfirmationPolicyInfo> confirmationPolicyInfos = confirmationPolicies.stream()
                                    .map(ConfirmationPolicyInfo::new)
                                    .collect(Collectors.toList());
                            List<CancellationPolicyInfo> cancellationPolicyInfos = cancellationPolicies.stream()
                                    .map(CancellationPolicyInfo::new)
                                    .collect(Collectors.toList());
                            List<RefundPolicyInfo> refundPolicyInfos = refundPolicies.stream()
                                    .map(RefundPolicyInfo::new)
                                    .collect(Collectors.toList());
                            List<JobImageDto> imageDtoList = jobImages.stream()
                                    .map(JobImageDto::new)
                                    .collect(Collectors.toList());

                            // Here we assume job has a getCategory method that returns the category name as a String
                            String category = job.getCategory();
                            // Create and return the JobInfo object with the policies
                            return new JobInfo(job, imageDtoList, category, confirmationPolicyInfos, cancellationPolicyInfos,
                                    projectDescriptionDto, refundPolicyInfos);
                        }))
                .switchIfEmpty(Mono.error(new Exception("Job not found")));
    }
    public Mono<ProjectDescriptionDto> getProjectDescription(Integer projectId) {
        return projectDescriptionRepository.findByProjectId(projectId)
                .flatMap(projectDescription -> Mono.zip(
                        locationDetailsRepository.findByProjectId(projectDescription.getId()),
                        projectTimelineRepository.findByProjectId(projectDescription.getId()),
                        workBreakdownRepository.findByProjectId(projectDescription.getId()).collectList(),
                        materialsEquipmentRepository.findByProjectId(projectDescription.getId()).collectList(),
                        laborRequirementsRepository.findByProjectId(projectDescription.getId()).collectList(),
                        complianceRegulationsRepository.findByProjectId(projectDescription.getId()),
                        siteConditionsRepository.findByProjectId(projectDescription.getId()),
                        qualityRequirementsRepository.findByProjectId(projectDescription.getId())
                ).flatMap(tuple1 -> Mono.zip(
                        Mono.just(tuple1),
                        budgetConstraintsRepository.findByProjectId(projectDescription.getId()),
                        riskFactorsRepository.findByProjectId(projectDescription.getId()),
                        changeOrderProcessRepository.findByProjectId(projectDescription.getId()),
                        paymentTermsRepository.findByProjectId(projectDescription.getId()),
                        bondingInsuranceRepository.findByProjectId(projectDescription.getId()),
                        warrantyInformationRepository.findByProjectId(projectDescription.getId())
                ).map(tuple2 -> {
                    Tuple8<LocationDetails, ProjectTimeline, List<WorkBreakdown>, List<MaterialsEquipment>, List<LaborRequirements>, ComplianceRegulations, SiteConditions, QualityRequirements> tuple8 = tuple2.getT1();
                    LocationDetails locationDetails = tuple8.getT1();
                    ProjectTimeline projectTimeline = tuple8.getT2();
                    List<WorkBreakdown> workBreakdown = tuple8.getT3();
                    List<MaterialsEquipment> materialsEquipment = tuple8.getT4();
                    List<LaborRequirements> laborRequirements = tuple8.getT5();
                    ComplianceRegulations complianceRegulations = tuple8.getT6();
                    SiteConditions siteConditions = tuple8.getT7();
                    QualityRequirements qualityRequirements = tuple8.getT8();
                    BudgetConstraints budgetConstraints = tuple2.getT2();
                    RiskFactors riskFactors = tuple2.getT3();
                    ChangeOrderProcess changeOrderProcess = tuple2.getT4();
                    PaymentTerms paymentTerms = tuple2.getT5();
                    BondingInsurance bondingInsurance = tuple2.getT6();
                    WarrantyInformation warrantyInformation = tuple2.getT7();

                    LocationDetailsDto locationDetailsDto = new LocationDetailsDto(locationDetails);
                    ProjectTimelineDto projectTimelineDto = new ProjectTimelineDto(projectTimeline);
                    List<WorkBreakdownDto> workBreakdownDto = workBreakdown.stream()
                            .map(WorkBreakdownDto::new)
                            .collect(Collectors.toList());
                    List<MaterialsEquipmentDto> materialsEquipmentDto = materialsEquipment.stream()
                            .map(MaterialsEquipmentDto::new)
                            .collect(Collectors.toList());
                    List<LaborRequirementsDto> laborRequirementsDto = laborRequirements.stream()
                            .map(LaborRequirementsDto::new)
                            .collect(Collectors.toList());
                    ComplianceRegulationsDto complianceRegulationsDto = new ComplianceRegulationsDto(complianceRegulations);
                    SiteConditionsDto siteConditionsDto = new SiteConditionsDto(siteConditions);
                    QualityRequirementsDto qualityRequirementsDto = new QualityRequirementsDto(qualityRequirements);
                    BudgetConstraintsDto budgetConstraintsDto = new BudgetConstraintsDto(budgetConstraints);
                    RiskFactorsDto riskFactorsDto = new RiskFactorsDto(riskFactors);
                    ChangeOrderProcessDto changeOrderProcessDto = new ChangeOrderProcessDto(changeOrderProcess);
                    PaymentTermsDto paymentTermsDto = new PaymentTermsDto(paymentTerms);
                    BondingInsuranceDto bondingInsuranceDto = new BondingInsuranceDto(bondingInsurance);
                    WarrantyInformationDto warrantyInformationDto = new WarrantyInformationDto(warrantyInformation);

                    return new ProjectDescriptionDto(projectDescription, locationDetailsDto, projectTimelineDto, workBreakdownDto, materialsEquipmentDto, laborRequirementsDto, complianceRegulationsDto, siteConditionsDto, qualityRequirementsDto, budgetConstraintsDto, riskFactorsDto, changeOrderProcessDto, paymentTermsDto, bondingInsuranceDto, warrantyInformationDto);
                })))
                .switchIfEmpty(Mono.error(new Exception("Project not found")));
    }

    public Mono<Job> addImageByJobId(Integer jobId, String guid) {
        return jobImageRepository.insertImages(jobId, guid).then(jobRepository.findJobById(jobId));
    }

    public Mono<Job> removeImageById(String uri) {
        return jobImageRepository.findJobImagesByGUID(uri)
                .flatMap(jobImage ->
                        jobImageRepository.deleteImages(jobImage.getJobId(), jobImage.getId())
                                .thenReturn(jobImage)
                )
                .flatMap(jobImage -> jobRepository.findJobById(jobImage.getJobId()));
    }
}