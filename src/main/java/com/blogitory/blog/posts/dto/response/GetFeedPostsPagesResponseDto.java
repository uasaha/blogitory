package com.blogitory.blog.posts.dto.response;

import com.blogitory.blog.commons.dto.Pages;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Get posts for feed with pages response Dto.
 *
 * @author woonseok
 * @since 1.0
 **/
@Getter
@AllArgsConstructor
public class GetFeedPostsPagesResponseDto {
  Long start;
  Pages<GetFeedPostsResponseDto> pages;
}
