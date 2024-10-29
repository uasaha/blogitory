package com.blogitory.blog.tempposts.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

/**
 * Get temp posts response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
public class GetTempPostsResponseDto {
  private final String tempPostsId;
  private final String title;
  private final LocalDateTime createdAt;

  /**
   * Constructor.
   *
   * @param tempPostsId id(UUID)
   * @param title       title
   * @param createdAt   created at
   */
  public GetTempPostsResponseDto(UUID tempPostsId, String title, LocalDateTime createdAt) {
    this.tempPostsId = tempPostsId.toString();
    this.title = title;
    this.createdAt = createdAt;
  }
}
