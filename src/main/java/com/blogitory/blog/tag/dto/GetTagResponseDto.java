package com.blogitory.blog.tag.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * GetTagListResponseDto.
 *
 * @author woonseok
 * @Date 2024-07-31
 * @since 1.0
 **/
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GetTagResponseDto {
  private String tagName;
}
