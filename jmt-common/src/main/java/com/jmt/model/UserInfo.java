package com.jmt.model;

import com.jmt.entity.Job;
import com.jmt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.List;
@Data
@NoArgsConstructor
public class UserInfo {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String nationality;
    private Date dateOfBirth;
    private String blobName;
    private List<String> roles;
    private List<Job> jobs;

    public UserInfo(User user, List<Job> jobs, List<String> roles) {
        firstname = user.getFirstname();
        lastname = user.getLastname();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
        address = user.getAddress();
        nationality = user.getNationality();
        dateOfBirth = user.getDob();
        blobName = user.getBlobName();
        this.roles = roles;
        this.jobs = jobs;
    }

}