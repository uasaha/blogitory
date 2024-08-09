package com.blogitory.blog.tempposts.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * Get temp posts response dto.
 *
 * @author woonseok
 * @Date 2024-08-05
 * @since 1.0
 **/
@Getter
@Setter
public class GetTempPostsResponseDto {
  private String tempPostsId;
  private String tempPostsTitle;
  private LocalDateTime createdAt;

  public GetTempPostsResponseDto(UUID tempPostsId, LocalDateTime createdAt) {
    this.tempPostsId = tempPostsId.toString();
    this.createdAt = createdAt;
  }
}
