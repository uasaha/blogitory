package com.blogitory.blog.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Dto for getting User's info from GitHub.
 *
 * @author woonseok
 * @since 1.0
 **/
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@NoArgsConstructor
public class GithubUserInfoResponseDto {
  //username
  private String login;
  private String id;
  private String avatarUrl;
  private String name;
}
