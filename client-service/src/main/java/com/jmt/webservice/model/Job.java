package com.jmt.webservice.model;

import lombok.Data;

import java.sql.Blob;
import java.sql.Time;

@Data
public class Job {
    private Integer id;
    private Integer userId;
    private String name;
    private String description;
    private String location;
    private Time createdDate;
    private boolean completed;
    private Blob image;
}
