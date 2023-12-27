package com.jmt.model;
import com.jmt.entity.JobImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class JobImageDto {
    private Integer id;
    private String guid;
    private String signed_Url;
    private Timestamp uploaded_date;

    public JobImageDto(JobImage jobImage) {
        id = jobImage.getId();
        guid = jobImage.getGuid();
        uploaded_date = jobImage.getUploaded_date();
    }
}
