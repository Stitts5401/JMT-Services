package com.jmt.webservice.model;

import ch.qos.logback.core.model.Model;
import com.jmt.webservice.literal.NationalityData;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo extends Model implements Validator {
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String nationality;
    private Date dateOfBirth;
    private String phoneNumber;
    private List<String> roles;
    private List<JobInfo> jobs;
    @Override
    public boolean supports(Class<?> clazz) {
        return UserInfo.class.isAssignableFrom(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        UserInfo userInfo = (UserInfo) target;

        if (userInfo.firstname == null || userInfo.firstname.isEmpty()) {
            errors.rejectValue("firstname", "firstname.empty", "First name cannot be empty");
        }
        if (userInfo.lastname == null || userInfo.lastname.isEmpty()) {
            errors.rejectValue("lastname", "lastname.empty", "Last name cannot be empty");
        }
        if (userInfo.email == null || !userInfo.email.contains("@")) {
            errors.rejectValue("email", "email.invalid", "Email is invalid");
        }
        if (userInfo.address == null || userInfo.address.trim().isEmpty()) {
            errors.rejectValue("address", "address.empty", "Address cannot be empty");
        }
        if (NationalityData.getCommonNationalities().stream().noneMatch(userInfo.nationality::equals)) {
            errors.rejectValue("nationality", "nationality.empty", "Nationality cannot be empty");
        }
        if (userInfo.dateOfBirth == null) {
            errors.rejectValue("dateOfBirth", "dateOfBirth.null", "Date of birth cannot be null");
        } else {
            // Assuming you want to check for adult age (18+)
            Date now = new Date();
            long age = now.getYear() - userInfo.dateOfBirth.getYear();
            if (age < 18) {
                errors.rejectValue("dateOfBirth", "dateOfBirth.invalid", "Must be at least 18 years old");
            }
        }
        if (userInfo.phoneNumber == null || !userInfo.phoneNumber.matches("[0-9]+")) {
            errors.rejectValue("phoneNumber", "phoneNumber.invalid", "Invalid phone number");
        }
    }
}