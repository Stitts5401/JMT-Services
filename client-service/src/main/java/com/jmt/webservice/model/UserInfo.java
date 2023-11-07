package com.jmt.webservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserInfo {
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String nationality;
    private Date dateOfBirth;
    private String phoneNumber;
    private List<String> roles;
    private List<Job> jobs;
}