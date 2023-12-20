package com.jmt.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("user")
public class User {
    @Id
    private Integer id;
    private String email;
    @Column("profile_picture")
    private String blobName;
    @Column("first_name")
    private String firstname;
    @Column("last_name")
    private String lastname;
    @Column("phone_number")
    private String phoneNumber;
    private String address;
    private String nationality;
    private Date dob;
    private boolean enabled;
    @Column("joined_date")
    private Date created;
    @Column("last_active_date")
    private Timestamp updated;
}