package com.blogitory.blog.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto, Blog list for using header.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class GetBlogInHeaderResponseDto {
  private String name;
  private String url;
}
