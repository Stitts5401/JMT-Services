package com.jmt.jobs.model;

import com.jmt.jobs.entity.CancellationPolicy;
import com.jmt.jobs.entity.Job;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobInfo {
    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    private String location;
    private Time createdDate; // Changed to LocalDateTime for better date-time API
    private boolean completed;
    private List<JobImageDto> images; // Renamed to images for consistency
    private String category;
    private List<ConfirmationPolicyInfo> confirmationPolicyInfos;
    private List<CancellationPolicyInfo> cancellationPolicyInfos;
    private List<RefundPolicyInfo> refundPolicyInfos;

    // Constructor
    public JobInfo(Job job, List<JobImageDto> images, String category,
                   List<ConfirmationPolicyInfo> confirmationPolicyInfos,
                   List<CancellationPolicyInfo> cancellationPolicyInfos,
                   List<RefundPolicyInfo> refundPolicyInfos) {
        this.id = job.getId();
        this.userId = job.getUserId(); // Assuming Job class has this field
        this.name = job.getName();
        this.description = job.getDescription();
        this.location = job.getLocation();
        this.createdDate = job.getCreatedDate(); // Assuming Job class has this field as LocalDateTime
        this.completed = job.isCompleted(); // Assuming Job class has this field
        this.images = images;
        this.category = category; // Assuming category is passed as a String, else you need to adjust
        this.confirmationPolicyInfos = confirmationPolicyInfos;
        this.cancellationPolicyInfos = cancellationPolicyInfos;
        this.refundPolicyInfos = refundPolicyInfos;
    }
}