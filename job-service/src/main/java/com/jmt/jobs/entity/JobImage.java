package com.jmt.jobs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("job_images")
public class JobImage {

    @Id
    private Integer id;
    @Column("job_id")
    private Integer jobId;
    private String guid;
    private Timestamp uploaded_date;
}
