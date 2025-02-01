package com.blogitory.blog.posts.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get related posts response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetRelatedPostsResponseDto {
  private Long postsNo;
  private String title;
  private String url;
  private LocalDateTime createdAt;
}
