package com.jmt.model;

import com.jmt.entity.PaymentTerms;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentTermsDto {
    private Integer paymentId;
    private Integer projectId;
    private String paymentSchedule;
    private String conditions;

    public PaymentTermsDto(PaymentTerms paymentTerms) {
        this.paymentId = paymentTerms.getId();
        this.projectId = paymentTerms.getProjectId();
        this.paymentSchedule = paymentTerms.getPaymentSchedule();
        this.conditions = paymentTerms.getConditions();
    }
}
