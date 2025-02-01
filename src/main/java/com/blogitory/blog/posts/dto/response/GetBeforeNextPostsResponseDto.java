package com.blogitory.blog.posts.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get before next posts dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetBeforeNextPostsResponseDto {
  private String beforeUrl;
  private String afterUrl;
  private Long nowPostsNo;
  private List<GetRelatedPostsResponseDto> relatedPosts;
}
