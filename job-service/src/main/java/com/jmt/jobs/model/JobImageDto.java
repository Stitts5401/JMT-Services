package com.jmt.jobs.model;
import com.jmt.jobs.entity.JobImage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class JobImageDto {
    private Integer id;
    private String guid;
    private Timestamp uploaded_date;

    public JobImageDto(JobImage jobImage) {
        id = jobImage.getId();
        guid = jobImage.getGuid();
        uploaded_date = jobImage.getUploaded_date();
    }
}
