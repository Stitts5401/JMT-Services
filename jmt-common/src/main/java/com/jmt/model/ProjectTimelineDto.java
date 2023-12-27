package com.jmt.model;

import com.jmt.entity.ProjectTimeline;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class ProjectTimelineDto {
    private Integer id;
    private Integer projectId;
    private Date startDate;
    private Date endDate;

    public ProjectTimelineDto( ProjectTimeline projectTimeline) {
        this.id = projectTimeline.getId();
        this.projectId = projectTimeline.getProjectId();
        this.startDate = projectTimeline.getStartDate();
        this.endDate = projectTimeline.getEndDate();
    }
}
