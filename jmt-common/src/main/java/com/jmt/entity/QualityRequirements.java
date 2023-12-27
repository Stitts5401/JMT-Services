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
@Table("quality_requirements")
public class QualityRequirements {
    private Integer id;
    private Integer projectId;
    private String standards;
    @Column("inspection_procedures")
    private String inspectionProcedures;
}
