package com.jmt.model;

import com.jmt.entity.LocationDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDetailsDto {


    private Integer id;
    private Integer projectId;
    private String street;
    private String city;
    private String state;
    private Integer zipcode;
    private String accessibility;

    public LocationDetailsDto(LocationDetails locationDetails) {
        this.id = locationDetails.getId();
        this.projectId = locationDetails.getProjectId();
        this.street = locationDetails.getStreet();
        this.city = locationDetails.getCity();
        this.state = locationDetails.getState();
        this.zipcode = locationDetails.getZipcode();
        this.accessibility = locationDetails.getAccessibility();
    }
}
