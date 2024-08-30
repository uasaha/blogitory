package com.blogitory.blog.follow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * GetFollowResponseDto.
 *
 * @author woonseok
 * @Date 2024-08-30
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetFollowResponseDto {
  private String thumb;
  private String username;
  private String name;
}
