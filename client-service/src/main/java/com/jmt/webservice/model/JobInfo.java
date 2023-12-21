package com.jmt.webservice.model;

import lombok.Data;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.sql.Time;
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

}