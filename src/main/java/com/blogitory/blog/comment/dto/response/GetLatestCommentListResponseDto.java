package com.blogitory.blog.comment.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * GetLatestCommentListResponseDto.
 *
 * @author woonseok
 * @Date 2024-07-31
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetLatestCommentListResponseDto {
  private String name;
  private String username;
  private String userPfp;
  private String postUrl;
  private String content;
  private LocalDateTime createdAt;
}
