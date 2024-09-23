package com.blogitory.blog.posts.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get post dto for managing.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class GetPostManageResponseDto {
  private String blogName;
  private String categoryName;
  private String postUrl;
  private String postTitle;
  private String postThumb;
  private LocalDateTime createdAt;
  private boolean isOpen;
}
