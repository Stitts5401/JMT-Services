package com.jmt.webservice.service;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class  ReactivePage<T> {

    private final List<T> content;
    private final Pageable pageable;
    private final long total;

    public ReactivePage(List<T> content, Pageable pageable, long total) {
        this.content = content;
        this.pageable = pageable;
        this.total = total;
    }

    // getters for content, pageable, and total
}