package com.blogitory.blog.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * GetLatestCommentListResponseDto.
 *
 * @author woonseok
 * @Date 2024-07-31
 * @since 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
public class GetLatestCommentListResponseDto {
  private Long postsNo;
  private String contents;
  private String createdAt;
}
