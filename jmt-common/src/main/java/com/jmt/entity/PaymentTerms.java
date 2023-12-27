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
@Table("payment_terms")
public class PaymentTerms {
    private Integer id;
    private Integer projectId;
    @Column("payment_schedule")
    private String paymentSchedule;
    private String conditions;
}
