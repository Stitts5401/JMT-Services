package com.stitts.apigateway.entity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(schema = "user_role")
public class Role {
        private Integer id;
        private String name;
}
