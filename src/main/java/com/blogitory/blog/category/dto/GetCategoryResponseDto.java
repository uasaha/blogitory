package com.blogitory.blog.category.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * GetCategoryListResponseDto.
 *
 * @author woonseok
 * @Date 2024-07-31
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GetCategoryResponseDto {
  private Long categoryNo;
  private String categoryName;
}
