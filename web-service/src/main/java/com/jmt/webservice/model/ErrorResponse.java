package com.jmt.webservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;


@Data
@NoArgsConstructor
public class ErrorResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("details")
    private String details;
}
