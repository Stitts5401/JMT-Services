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
@Table("change_order_process")
public class ChangeOrderProcess {
    private Integer id;
    private Integer projectId;
    @Column("change_procedures")
    private String changeProcedures;
}
