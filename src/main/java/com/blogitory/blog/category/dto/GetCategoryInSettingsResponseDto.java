package com.blogitory.blog.category.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Dto for getting category.
 *
 * @author woonseok
 * @since 1.0
 **/
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class GetCategoryInSettingsResponseDto {
  private Long categoryNo;
  private String name;
  private boolean deleted;
}
