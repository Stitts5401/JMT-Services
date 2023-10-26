package com.stitts.apigateway.entity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
public class Role {
        @Id
        private Integer id;
        private String name;
}
