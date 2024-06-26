package com.blogitory.blog.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response Dto for Blog's profile.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class BlogProfileResponseDto {
  private String blogUrl;
  private String blogName;
  private String blogBio;
}
