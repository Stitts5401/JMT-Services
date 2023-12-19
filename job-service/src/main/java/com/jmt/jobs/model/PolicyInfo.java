package com.jmt.jobs.model;

import com.jmt.jobs.entity.Policy;
import com.jmt.jobs.entity.PolicyItem;
import lombok.Data;

@Data
public class PolicyInfo {
    private Integer id;
    private String section;
    private String content;

    public PolicyInfo(PolicyItem policy) {
        this.id = policy.getId();
        section = policy.getSection();
        content = policy.getContent();
    }
}