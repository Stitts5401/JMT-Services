package com.jmt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("location_details")
public class LocationDetails {
    private Integer id;
    private Integer projectId;
    private String street;
    private String city;
    private String state;
    private Integer zipcode;
    private String accessibility;
}
