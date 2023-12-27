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
@Table("bonding_insurance")
public class BondingInsurance {
    private Integer id;
    private Integer projectId;
    @Column("bonding_requirements")
    private String bondingRequirements;
    @Column("insurance_requirements")
    private String insuranceRequirements;
}
