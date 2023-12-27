package com.jmt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("budget_constraints")
public class BudgetConstraints {
    private Integer id;
    private Integer projectId;
    @Column("budget_limit")
    private BigDecimal budgetLimit;
    @Column("cost_targets")
    private BigDecimal costTargets;
}
