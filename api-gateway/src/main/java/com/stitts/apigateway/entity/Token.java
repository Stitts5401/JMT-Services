package com.stitts.apigateway.entity;

import com.stitts.apigateway.config.security.ReactiveUserDetails;
import com.stitts.apigateway.token.TokenType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(schema = "token")
public class Token {

  @Id
  public Integer id;
  public String token;
  public TokenType tokenType;
  public boolean revoked;
  public boolean expired;
  public ReactiveUserDetails reactiveUserDetails;
}
