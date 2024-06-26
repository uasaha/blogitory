package com.blogitory.blog.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto for getting GitHub Access Token.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@Setter
@AllArgsConstructor
public class GithubAccessTokenResponseDto {
  private String accessToken;
}
