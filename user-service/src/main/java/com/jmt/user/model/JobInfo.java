package com.jmt.user.model;

import com.jmt.user.entity.Job;
import lombok.Builder;
import lombok.Data;

import java.nio.ByteBuffer;
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
    private String image;
    private String category;
    private List<PolicyInfo> policyInfo;

}