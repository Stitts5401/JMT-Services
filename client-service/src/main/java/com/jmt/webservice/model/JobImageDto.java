package com.jmt.webservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
public class JobImageDto {
    private Integer id;
    private String guid;
    private String signed_Url;
    private Timestamp uploaded_date;
}
