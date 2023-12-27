package com.jmt.model;

import com.jmt.entity.WorkBreakdown;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class WorkBreakdownDto {
    private Integer id;
    private Integer projectId;
    private String taskDescription;
    private Integer duration;

    public WorkBreakdownDto(WorkBreakdown workBreakdown) {
        this.id = workBreakdown.getId();
        this.projectId = workBreakdown.getProjectId();
        this.taskDescription = workBreakdown.getTaskDescription();
        this.duration = workBreakdown.getDuration();
    }
}
