package com.stitts.apigateway.entity;

import com.stitts.apigateway.token.TokenType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
public class Token {

  @Id
  public Integer id;
  @Column("token")
  public String tokenValue;
  public TokenType tokenType;
  public boolean revoked;
  public boolean expired;
  @Column("user_id")
  public Integer userId;
}
