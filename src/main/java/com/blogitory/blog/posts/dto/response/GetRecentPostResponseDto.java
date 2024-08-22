package com.blogitory.blog.posts.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get Recent Post Response dto.
 *
 * @author woonseok
 * @Date 2024-08-22
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class GetRecentPostResponseDto {
  private String blogUrl;
  private String blogName;
  private String username;
  private String blogPfp;
  private String postUrl;
  private String title;
  private String summary;
  private String thumb;
  private LocalDateTime createdAt;
  private Long heart;
  private Long comment;
}
