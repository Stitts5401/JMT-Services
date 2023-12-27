package com.jmt.model;

import com.jmt.entity.ChangeOrderProcess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeOrderProcessDto {
    private Integer changeId;
    private Integer projectId;
    private String changeProcedures;

    public ChangeOrderProcessDto(ChangeOrderProcess changeOrderProcess) {
        this.changeId = changeOrderProcess.getId();
        this.projectId = changeOrderProcess.getProjectId();
        this.changeProcedures = changeOrderProcess.getChangeProcedures();
    }
}
