package com.jmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JobInfoPage {
    private List<JobInfo> content;
    private int number; // current page
    private int size; // page size
    private long totalElements;
}