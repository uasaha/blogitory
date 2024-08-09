package com.blogitory.blog.blog.dto.response;

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
public class GetBlogInProfileResponseDto {
  private String blogUrl;
  private String blogName;
  private String blogBio;
}
