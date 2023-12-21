package com.jmt.jobs.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Time;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("job")
public class Job {
    @Id
    private Integer id;
    @Column("user_id")
    private Integer userId;
    private String name;
    private String description;
    private String location;
    @Column("created_date")
    private Time createdDate;
    @Column("completion_status")
    private boolean completed;
    private String category;
}
