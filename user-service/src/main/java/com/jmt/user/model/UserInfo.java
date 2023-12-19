package com.jmt.user.model;

import com.jmt.user.entity.User;
import lombok.Data;

import java.sql.Date;
import java.util.List;
@Data
public class UserInfo {
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String nationality;
    private Date dateOfBirth;
    private List<String> roles;
    private List<JobInfo> jobs;
    public UserInfo(User user, List<JobInfo> jobs, List<String> roles) {
        firstname = user.getFirstname();
        lastname = user.getLastname();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
        address = user.getAddress();
        nationality = user.getNationality();
        this.roles = roles;
        this.jobs = jobs;
    }
}