package com.blogitory.blog.comment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get comment response dto.
 *
 * @author woonseok
 * @Date 2024-08-08
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetCommentResponseDto {
  private String name;
  private String username;
  private String userPfp;
  private Long commentNo;
  private String content;
  private boolean deleted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long childCnt;
}
