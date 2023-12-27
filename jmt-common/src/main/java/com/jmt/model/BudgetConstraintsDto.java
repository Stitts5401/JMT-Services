package com.jmt.model;

import com.jmt.entity.BudgetConstraints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BudgetConstraintsDto {
    private Integer budgetId;
    private Integer projectId;
    private BigDecimal budgetLimit;
    private BigDecimal costTargets;

    public BudgetConstraintsDto(BudgetConstraints budgetConstraints) {
        this.budgetId = budgetConstraints.getId();
        this.projectId = budgetConstraints.getProjectId();
        this.budgetLimit = budgetConstraints.getBudgetLimit();
        this.costTargets = budgetConstraints.getCostTargets();
    }
}
