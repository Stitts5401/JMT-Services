package com.stitts.apigateway.model;

import com.stitts.apigateway.entity.Job;
import com.stitts.apigateway.entity.User;
import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;

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
    private List<Job> jobs;
    public UserInfo(User user, List<Job> jobs, List<String> roles) {
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