package com.blogitory.blog.category.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * GetCategoryListResponseDto.
 *
 * @author woonseok
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GetCategoryResponseDto {
  private Long categoryNo;
  private String categoryName;
  private boolean deleted;
  private long postsCnt;
}
