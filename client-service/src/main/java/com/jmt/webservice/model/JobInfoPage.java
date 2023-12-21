package com.jmt.webservice.model;

import lombok.Data;

import java.util.List;

@Data
public class JobInfoPage {
    private List<JobInfo> content;
    private int number; // current page
    private int size; // page size
    private long totalElements;
}