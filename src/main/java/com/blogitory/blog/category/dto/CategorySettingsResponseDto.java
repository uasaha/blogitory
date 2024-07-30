package com.blogitory.blog.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto for getting category.
 *
 * @author woonseok
 * @Date 2024-07-15
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
public class CategorySettingsResponseDto {
  private Long categoryNo;
  private String name;
  private boolean deleted;
}
