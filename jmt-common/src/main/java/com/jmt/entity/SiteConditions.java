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
@Table("site_conditions")
public class SiteConditions {
    private Integer id;
    private Integer projectId;
    @Column("survey_results")
    private String surveyResults;
    private String challenges;
}
