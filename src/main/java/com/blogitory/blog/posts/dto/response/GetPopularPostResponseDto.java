package com.blogitory.blog.posts.dto.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Get popular post response dto.
 *
 * @author woonseok
 * @Date 2024-08-27
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class GetPopularPostResponseDto {
  private String url;
  private String thumb;
  private String title;
  private String summary;
  private Long heartCnt;
  private Long commentCnt;
}
