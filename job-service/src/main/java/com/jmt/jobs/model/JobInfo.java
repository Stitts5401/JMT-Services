package com.jmt.jobs.model;

import com.jmt.jobs.entity.Job;
import lombok.Data;

import java.sql.Time;
import java.util.List;

@Data
public class JobInfo {
    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    private String location;
    private Time createdDate;
    private boolean completed;
    private List<JobImageDto> image;
    private String category;
    private List<PolicyInfo> policyInfo;

    public JobInfo(Job job, List<PolicyInfo> policyInfo, List<JobImageDto> images) {
        id = job.getId();
        userId = job.getUserId();
        name = job.getName();
        description = job.getDescription();
        location = job.getLocation();
        createdDate = job.getCreatedDate();
        completed = job.isCompleted();
        category = job.getCategory();
        this.policyInfo = policyInfo;
        this.image = images;
    }

}