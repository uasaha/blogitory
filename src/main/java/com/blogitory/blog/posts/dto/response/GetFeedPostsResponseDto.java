package com.blogitory.blog.posts.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get posts for feed response Dto.
 *
 * @author woonseok
 * @Date 2024-09-05
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetFeedPostsResponseDto {
  private String username;
  private String title;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String details;
  private String postsUrl;
  private String blogUrl;
  private String blogName;
  private String blogThumb;
}
