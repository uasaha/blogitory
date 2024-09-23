package com.blogitory.blog.comment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get child comment response dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetChildCommentResponseDto {
  private String name;
  private String username;
  private String userPfp;
  private Long parentNo;
  private Long commentNo;
  private String content;
  private boolean deleted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
