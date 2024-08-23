package com.blogitory.blog.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for getting GitHub Access Token.
 *
 * @author woonseok
 * @since 1.0
 **/
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor
public class GithubAccessTokenResponseDto {
  private String accessToken;
  private String tokenType;
  private String scope;
}
