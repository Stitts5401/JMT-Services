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
@Table("warranty_information")
public class WarrantyInformation {
    private Integer id;
    private Integer projectId;
    @Column("warranty_details")
    private String warrantyDetails;
}
