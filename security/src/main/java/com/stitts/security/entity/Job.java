package com.stitts.security.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.util.Date;

@Data
@NoArgsConstructor
@Table("jobs")
public class Job {
    @Id
    @Column("job_id")
    private Long id;
    @Column("user_id")
    private Long userId;
    private String category;
    @Column("job_date")
    private Date jobDate;
    private String description;
    private byte[] image;
    @Column("completion_status")
    private Boolean completionStatus;
    @Column("creation_date")
    private Date creationDate;
}


