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
@Table("materials_equipment")
public class MaterialsEquipment {
    private Integer id;
    private Integer projectId;
    @Column("material_name")
    private String materialName;
    private Integer quantity;
    @Column("equipment_needed")
    private String equipmentNeeded;
    private String provider;
    private String units;
}
