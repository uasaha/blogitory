package com.blogitory.blog.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for getting User's info from GitHub.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GithubUserInfoResponseDto {
  private String thumbnail;
  private String email;
  private String username;
}
