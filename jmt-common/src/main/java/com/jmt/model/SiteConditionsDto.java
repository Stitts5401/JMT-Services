package com.jmt.model;

import com.jmt.entity.SiteConditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class SiteConditionsDto {
    private Integer conditionId;
    private Integer projectId;
    private String surveyResults;
    private String challenges;

    public SiteConditionsDto(SiteConditions siteConditions) {
        this.conditionId = siteConditions.getId();
        this.projectId = siteConditions.getProjectId();
        this.surveyResults = siteConditions.getSurveyResults();
        this.challenges = siteConditions.getChallenges();
    }
}
