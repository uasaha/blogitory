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
public class GetCommentListResponseDto {
  private Long commentNo;
  private Long parentNo;
  private String name;
  private String username;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public GetCommentListResponseDto(Long commentNo,
                                   String name,
                                   String username,
                                   String content,
                                   LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {
    this.commentNo = commentNo;
    this.name = name;
    this.username = username;
    this.content = content;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
