package com.jmt.user.model;

import com.jmt.user.entity.Job;
import lombok.Builder;
import lombok.Data;

import java.nio.ByteBuffer;
import java.sql.Time;

@Data
public class JobInfo {
    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    private String location;
    private Time createdDate;
    private boolean completed;
    private ByteBuffer image;
    private String category;

    public JobInfo(Job job) {
        id = job.getId();
        userId = job.getUserId();
        name = job.getName();
        description = job.getDescription();
        location = job.getLocation();
        createdDate = job.getCreatedDate();
        completed = job.isCompleted();
        image = job.getImage();
        category = job.getCategory();
    }

}