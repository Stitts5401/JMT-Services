package com.jmt.model;

import com.jmt.entity.MaterialsEquipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialsEquipmentDto {
    private Integer id;
    private Integer projectId;
    private String materialName;
    private Integer quantity;
    private String equipmentNeeded;
    private String provider;
    private String units;

    public MaterialsEquipmentDto(MaterialsEquipment materialsEquipment) {
        this.id = materialsEquipment.getId();
        this.projectId = materialsEquipment.getProjectId();
        this.materialName = materialsEquipment.getMaterialName();
        this.quantity = materialsEquipment.getQuantity();
        this.equipmentNeeded = materialsEquipment.getEquipmentNeeded();
        this.provider = materialsEquipment.getProvider();
        this.units = materialsEquipment.getUnits();
    }
}
