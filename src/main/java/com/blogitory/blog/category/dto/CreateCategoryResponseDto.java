package com.blogitory.blog.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for create category.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class CreateCategoryResponseDto {
  private Long categoryNo;
  private String name;
}
