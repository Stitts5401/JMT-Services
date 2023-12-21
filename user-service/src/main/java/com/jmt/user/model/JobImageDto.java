package com.jmt.user.model;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class JobImageDto {
    private Integer id;
    private String guid;
    private Timestamp uploaded_date;
}
